export interface CreateBookingRequest {
  courtId: string;
  startTime: string; // ISO OffsetDateTime
  endTime: string;
  gameMode: string;
  totalPrice: number;
}

export interface CreateBookingResponse {
  bookingId: string;
}
