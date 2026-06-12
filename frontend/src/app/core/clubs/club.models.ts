export interface CityResult {
  city: string;
  clubCount: number;
}

export interface ClubSummary {
  id: string;
  name: string;
  city: string;
  addressLine1: string;
  courtCount: number;
}

export interface CourtSummary {
  id: string;
  name: string;
  courtType: string;
}

export interface ClubDetail {
  id: string;
  name: string;
  city: string;
  addressLine1: string;
  addressLine2?: string;
  zipCode: string;
  courts: CourtSummary[];
}

export interface ClubSearchResponse {
  cities: CityResult[];
  clubs: ClubSummary[];
}

export interface SlotEntry {
  courtId: string;
  startTime: string;   // "08:00"
  endTime: string;     // "09:00"
  intervalMinutes: number;
  price: number;
  memberPrice: number;
  currency: string;
  gameMode: string;
}
