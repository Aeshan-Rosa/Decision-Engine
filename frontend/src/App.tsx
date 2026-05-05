import { useEffect, useMemo, useState } from "react";
import { choose, getEnding, getHistory, startGame } from "./api";
import { Ending, GameState, HistoryItem } from "./types";

const statKeys: Array<keyof GameState["stats"]> = [
  "money",
  "intelligence",
  "happiness",
  "health",
  "reputation",
  "risk",
  "relationships",
  "age"
];

export default function App() {
  const [state, setState] = useState<GameState | null>(null);
  const [history, setHistory] = useState<HistoryItem[]>([]);
  const [ending, setEnding] = useState<Ending | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const start = async () => {
    setLoading(true);
    setError(null);
    try {
      const game = await startGame();
      setState(game);
      setHistory([]);
      setEnding(null);
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  };

  const makeChoice = async (choiceId: number) => {
    if (!state) return;
    setLoading(true);
    setError(null);
    try {
      const next = await choose(state.gameId, choiceId);
      setState(next);
      const h = await getHistory(next.gameId);
      setHistory(h);
      if (next.status === "FINISHED") {
        setEnding(await getEnding(next.gameId));
      }
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (state) {
      getHistory(state.gameId).then(setHistory).catch(() => undefined);
    }
  }, [state?.gameId]);

  const stageLabel = useMemo(() => state?.currentStage.replace(/_/g, " ") ?? "", [state?.currentStage]);

  if (!state) {
    return (
      <main className="min-h-screen bg-slate-950 text-slate-100 grid place-items-center p-6">
        <section className="max-w-2xl rounded-3xl border border-slate-800 bg-slate-900/80 p-10 shadow-2xl">
          <h1 className="text-5xl font-bold tracking-tight text-cyan-300">LifePath</h1>
          <p className="mt-4 text-slate-300">A decision-based life simulator where every choice writes your destiny.</p>
          <button onClick={start} disabled={loading} className="mt-8 rounded-xl bg-cyan-500 px-6 py-3 font-semibold text-slate-950 hover:bg-cyan-400 transition">
            {loading ? "Starting..." : "Start New Game"}
          </button>
          {error && <p className="mt-3 text-rose-400">{error}</p>}
        </section>
      </main>
    );
  }

  if (state.status === "FINISHED") {
    return (
      <main className="min-h-screen bg-slate-950 text-slate-100 p-6">
        <div className="mx-auto max-w-4xl rounded-3xl border border-slate-800 bg-slate-900 p-8">
          <h2 className="text-sm uppercase text-cyan-300">Your Ending</h2>
          <h1 className="text-4xl font-bold mt-2">{ending?.title ?? "Final Chapter"}</h1>
          <p className="mt-4 text-slate-300">{ending?.description ?? "Life moves in many directions."}</p>
          <button onClick={start} className="mt-8 rounded-xl bg-cyan-500 px-6 py-3 font-semibold text-slate-950 hover:bg-cyan-400 transition">Play Again</button>
        </div>
      </main>
    );
  }

  return (
    <main className="min-h-screen bg-slate-950 text-slate-100 p-6">
      <div className="mx-auto grid max-w-7xl gap-4 lg:grid-cols-4">
        <aside className="rounded-2xl border border-slate-800 bg-slate-900 p-4 lg:col-span-1">
          <h2 className="text-lg font-bold">Player Stats</h2>
          <p className="text-xs text-slate-400">Stage: {stageLabel}</p>
          <div className="mt-4 space-y-3">
            {statKeys.map((k) => {
              const val = state.stats[k] as number;
              const bar = k === "money" || k === "age" ? Math.min(100, Math.max(0, val)) : val;
              return (
                <div key={k}>
                  <div className="flex justify-between text-sm capitalize">
                    <span>{k}</span><span>{val}</span>
                  </div>
                  <div className="h-2 rounded-full bg-slate-800 mt-1 overflow-hidden">
                    <div className="h-full bg-cyan-400 transition-all duration-500" style={{ width: `${bar}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
        </aside>

        <section className="rounded-2xl border border-slate-800 bg-slate-900 p-6 lg:col-span-2">
          <h2 className="text-xs uppercase text-cyan-300">Current Event</h2>
          <h1 className="mt-2 text-3xl font-semibold">{state.currentEvent?.title}</h1>
          <p className="mt-3 text-slate-300">{state.currentEvent?.description}</p>
          <div className="mt-6 space-y-3">
            {state.currentEvent?.choices.map((choice) => (
              <button
                key={choice.id}
                disabled={loading}
                onClick={() => makeChoice(choice.id)}
                className="w-full rounded-xl border border-slate-700 bg-slate-800/70 p-4 text-left hover:border-cyan-400 hover:bg-slate-800 transition"
              >
                <p className="font-semibold">{choice.text}</p>
                <p className="text-xs text-slate-400 mt-1">Choose and evolve your story</p>
              </button>
            ))}
          </div>
          {error && <p className="mt-3 text-rose-400">{error}</p>}
        </section>

        <aside className="rounded-2xl border border-slate-800 bg-slate-900 p-4 lg:col-span-1">
          <h2 className="text-lg font-bold">Timeline</h2>
          <div className="mt-4 space-y-3 max-h-[70vh] overflow-y-auto pr-1">
            {history.length === 0 && <p className="text-sm text-slate-500">No decisions yet.</p>}
            {history.map((item) => (
              <div key={item.id} className="rounded-lg border border-slate-800 bg-slate-800/40 p-3">
                <p className="text-xs text-cyan-300">{item.eventTitle}</p>
                <p className="text-sm mt-1">{item.choiceText}</p>
              </div>
            ))}
          </div>
        </aside>
      </div>
    </main>
  );
}
