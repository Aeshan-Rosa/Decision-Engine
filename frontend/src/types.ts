export type Stage = "SCHOOL" | "UNIVERSITY" | "EARLY_CAREER" | "ADULT_LIFE" | "ENDING";

export type Stats = {
  money: number;
  intelligence: number;
  happiness: number;
  health: number;
  reputation: number;
  risk: number;
  relationships: number;
  age: number;
  currentStage: Stage;
};

export type Choice = {
  id: number;
  text: string;
  moneyDelta: number;
  intelligenceDelta: number;
  happinessDelta: number;
  healthDelta: number;
  reputationDelta: number;
  riskDelta: number;
  relationshipsDelta: number;
  ageDelta: number;
};

export type GameEvent = {
  id: number;
  title: string;
  description: string;
  stage: Stage;
  choices: Choice[];
};

export type GameState = {
  gameId: number;
  status: "ACTIVE" | "FINISHED";
  currentStage: Stage;
  stats: Stats;
  currentEvent: GameEvent | null;
};

export type HistoryItem = {
  id: number;
  eventId: number;
  eventTitle: string;
  choiceId: number;
  choiceText: string;
  decidedAt: string;
};

export type Ending = {
  title: string;
  description: string;
  theme: string;
};
