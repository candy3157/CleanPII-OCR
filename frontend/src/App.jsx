import { useEffect, useState } from "react";

const menuItems = [
  { id: "dashboard", label: "대시보드" },
  { id: "documents", label: "문서 목록" },
  { id: "review", label: "검수 화면" },
  { id: "policies", label: "정책 관리" },
  { id: "audit", label: "감사 로그" }
];

export default function App() {
  const [health, setHealth] = useState("확인 중...");
  const [user, setUser] = useState(null);
  const [summary, setSummary] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [selectedDocument, setSelectedDocument] = useState(null);
  const [policies, setPolicies] = useState([]);
  const [logs, setLogs] = useState([]);
  const [activeView, setActiveView] = useState("dashboard");

  useEffect(() => {
    async function load() {
      try {
        const [
          healthResponse,
          userResponse,
          summaryResponse,
          documentsResponse,
          policiesResponse,
          logsResponse
        ] = await Promise.all([
          fetch("/api/health"),
          fetch("/api/auth/me"),
          fetch("/api/dashboard/summary"),
          fetch("/api/documents"),
          fetch("/api/policies"),
          fetch("/api/audit-logs")
        ]);

        const healthData = await healthResponse.json();
        const userData = await userResponse.json();
        const summaryData = await summaryResponse.json();
        const documentsData = await documentsResponse.json();
        const policiesData = await policiesResponse.json();
        const logsData = await logsResponse.json();

        setHealth(healthData.status);
        setUser(userData);
        setSummary(summaryData);
        setDocuments(documentsData.documents);
        setPolicies(policiesData.policies);
        setLogs(logsData.logs);

        if (documentsData.documents.length > 0) {
          const detailResponse = await fetch(
            `/api/documents/${documentsData.documents[0].id}`
          );
          setSelectedDocument(await detailResponse.json());
        }
      } catch {
        setHealth("백엔드 연결 실패");
      }
    }

    load();
  }, []);

  async function openDocument(documentId) {
    const response = await fetch(`/api/documents/${documentId}`);
    const detail = await response.json();
    setSelectedDocument(detail);
    setActiveView("review");
  }

  async function reviewAction(action) {
    if (!selectedDocument) {
      return;
    }

    const response = await fetch(
      `/api/documents/${selectedDocument.id}/reviews/${action}`,
      { method: "POST" }
    );
    const result = await response.json();
    alert(result.message);
  }

  return (
    <div className="console-shell">
      <aside className="sidebar">
        <div>
          <p className="brand-kicker">CleanPII OCR</p>
          <h1>운영 콘솔 MVP</h1>
          <p className="sidebar-copy">
            요구사항 명세서 기준으로 구성한 개인정보 비식별화 운영 화면 골격입니다.
          </p>
        </div>

        <nav className="nav-list">
          {menuItems.map((item) => (
            <button
              key={item.id}
              type="button"
              className={activeView === item.id ? "nav-item active" : "nav-item"}
              onClick={() => setActiveView(item.id)}
            >
              {item.label}
            </button>
          ))}
        </nav>

        <div className="sidebar-footer">
          <span>Backend</span>
          <strong>{health}</strong>
        </div>
      </aside>

      <main className="main-panel">
        <header className="topbar">
          <div>
            <p className="eyebrow">OCR 기반 개인정보 비식별화·검수 플랫폼</p>
            <h2>MVP 운영 화면 프로토타입</h2>
          </div>

          {user && (
            <div className="user-card">
              <span>{user.role}</span>
              <strong>{user.displayName}</strong>
            </div>
          )}
        </header>

        {summary && (
          <section className="metric-grid">
            <MetricCard label="전체 문서" value={summary.totalDocuments} />
            <MetricCard label="실패 건수" value={summary.failedDocuments} />
            <MetricCard label="검수 대기" value={summary.pendingReviews} />
            <MetricCard label="활성 정책" value={policies.length} />
          </section>
        )}

        {activeView === "dashboard" && summary && (
          <section className="content-grid">
            <article className="panel">
              <div className="panel-header">
                <h3>일별 처리 추이</h3>
                <span>최근 5일</span>
              </div>
              <div className="trend-list">
                {summary.dailyProcessed.map((item) => (
                  <div key={item.date} className="trend-item">
                    <span>{item.date}</span>
                    <strong>{item.count}건</strong>
                  </div>
                ))}
              </div>
            </article>

            <article className="panel">
              <div className="panel-header">
                <h3>개인정보 유형별 탐지</h3>
                <span>Mock 통계</span>
              </div>
              <div className="chip-grid">
                {summary.detectionCounts.map((item) => (
                  <div key={item.type} className="type-chip">
                    <span>{item.type}</span>
                    <strong>{item.count}</strong>
                  </div>
                ))}
              </div>
            </article>
          </section>
        )}

        {activeView === "documents" && (
          <section className="panel">
            <div className="panel-header">
              <h3>문서 목록</h3>
              <span>상태/업로더/파일명 기준 확장 예정</span>
            </div>
            <div className="table-shell">
              <table>
                <thead>
                  <tr>
                    <th>문서 ID</th>
                    <th>파일명</th>
                    <th>형식</th>
                    <th>업로더</th>
                    <th>상태</th>
                    <th>탐지 수</th>
                  </tr>
                </thead>
                <tbody>
                  {documents.map((document) => (
                    <tr
                      key={document.id}
                      onClick={() => openDocument(document.id)}
                    >
                      <td>{document.id}</td>
                      <td>{document.fileName}</td>
                      <td>{document.fileType}</td>
                      <td>{document.uploadedBy}</td>
                      <td>
                        <span className={`status-badge ${document.status.toLowerCase()}`}>
                          {document.status}
                        </span>
                      </td>
                      <td>{document.piiCount}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}

        {activeView === "review" && selectedDocument && (
          <section className="content-grid">
            <article className="panel">
              <div className="panel-header">
                <h3>문서 상세</h3>
                <span>{selectedDocument.fileName}</span>
              </div>
              <div className="detail-stack">
                <p><strong>문서 ID</strong> {selectedDocument.id}</p>
                <p><strong>상태</strong> {selectedDocument.status}</p>
                <p><strong>업로더</strong> {selectedDocument.uploadedBy}</p>
                <p><strong>업로드 시각</strong> {selectedDocument.uploadedAt}</p>
                <p><strong>미리보기 경로</strong> {selectedDocument.previewUrl}</p>
              </div>

              <div className="review-actions">
                <button type="button" onClick={() => reviewAction("approve")}>
                  검수 확정
                </button>
                <button
                  type="button"
                  className="secondary"
                  onClick={() => reviewAction("reject")}
                >
                  반려 처리
                </button>
              </div>
            </article>

            <article className="panel">
              <div className="panel-header">
                <h3>OCR / 탐지 결과</h3>
                <span>검수자 수정 기능 확장 예정</span>
              </div>

              <div className="section-block">
                <h4>OCR 텍스트</h4>
                {selectedDocument.ocrPages.map((page) => (
                  <div key={page.pageNumber} className="text-card">
                    <p>Page {page.pageNumber}</p>
                    <strong>신뢰도 {page.confidence}</strong>
                    <pre>{page.extractedText}</pre>
                  </div>
                ))}
              </div>

              <div className="section-block">
                <h4>탐지된 개인정보</h4>
                {selectedDocument.piiItems.map((item) => (
                  <div key={item.id} className="pii-row">
                    <div>
                      <span>{item.type}</span>
                      <strong>{item.originalValue}</strong>
                    </div>
                    <div>
                      <span>Masked</span>
                      <strong>{item.maskedValue}</strong>
                    </div>
                    <div>
                      <span>{item.detectionMethod}</span>
                      <strong>{item.location}</strong>
                    </div>
                  </div>
                ))}
              </div>
            </article>
          </section>
        )}

        {activeView === "policies" && (
          <section className="panel">
            <div className="panel-header">
              <h3>정책 관리</h3>
              <span>탐지/마스킹 정책 버전 골격</span>
            </div>
            <div className="policy-grid">
              {policies.map((policy) => (
                <article key={policy.id} className="policy-card">
                  <p>{policy.targetType}</p>
                  <h4>{policy.name}</h4>
                  <code>{policy.ruleExpression}</code>
                  <div className="policy-meta">
                    <span>{policy.maskingMode}</span>
                    <span>v{policy.version}</span>
                    <span>{policy.enabled ? "활성" : "비활성"}</span>
                  </div>
                </article>
              ))}
            </div>
          </section>
        )}

        {activeView === "audit" && (
          <section className="panel">
            <div className="panel-header">
              <h3>감사 로그</h3>
              <span>로그인, 정책 변경, 검수, 다운로드</span>
            </div>
            <div className="log-list">
              {logs.map((log) => (
                <div key={log.id} className="log-item">
                  <div>
                    <p>{log.actionType}</p>
                    <strong>{log.actor}</strong>
                  </div>
                  <div>
                    <p>{log.targetType}</p>
                    <strong>{log.targetId}</strong>
                  </div>
                  <div>
                    <p>{log.createdAt}</p>
                    <strong>{log.details}</strong>
                  </div>
                </div>
              ))}
            </div>
          </section>
        )}
      </main>
    </div>
  );
}

function MetricCard({ label, value }) {
  return (
    <article className="metric-card">
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}
