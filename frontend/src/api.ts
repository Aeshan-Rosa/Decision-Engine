import { Ending, GameState, HistoryItem } from "./types";

const API = "http://localhost:8080/api/games";

async function handle<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.error ?? "Request failed");
  }
  return res.json();
}

export async function startGame(): Promise<GameState> {
  const res = await fetch(`${API}/start`, { method: "POST" });
  const data = await handle<{ gameId: number; state: GameState }>(res);
  return data.state;
}

export async function getGame(gameId: number): Promise<GameState> {
  const res = await fetch(`${API}/${gameId}`);
  return handle<GameState>(res);
}

export async function choose(gameId: number, choiceId: number): Promise<GameState> {
  const res = await fetch(`${API}/${gameId}/choose`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ choiceId })
  });
  return handle<GameState>(res);
}

export async function getHistory(gameId: number): Promise<HistoryItem[]> {
  const res = await fetch(`${API}/${gameId}/history`);
  return handle<HistoryItem[]>(res);
}

export async function getEnding(gameId: number): Promise<Ending> {
  const res = await fetch(`${API}/${gameId}/ending`);
  return handle<Ending>(res);
}
