import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClubService } from '../../core/clubs/club.service';
import { BookingService } from '../../core/bookings/booking.service';
import { AuthService } from '../../core/auth/auth.service';
import { ClubDetail, CourtSummary, SlotEntry } from '../../core/clubs/club.models';
import { NgClass } from '@angular/common';

export interface TimeSlot {
  hour: number;
  label: string;
}

export interface SlotKey {
  courtId: string;
  hour: number;
  entry: SlotEntry;
}

const HOURS: TimeSlot[] = Array.from({ length: 14 }, (_, i) => ({
  hour: 8 + i,
  label: `${String(8 + i).padStart(2, '0')}:00`
}));

@Component({
  selector: 'app-club-detail',
  standalone: true,
  imports: [NgClass],
  templateUrl: './club-detail.component.html',
  styleUrl: './club-detail.component.css'
})
export class ClubDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private clubService = inject(ClubService);
  private bookingService = inject(BookingService);
  private authService = inject(AuthService);

  club = signal<ClubDetail | null>(null);
  loading = signal(true);
  error = signal('');

  slots = signal<SlotEntry[]>([]);
  slotsLoading = signal(false);

  today = new Date();
  dates = Array.from({ length: 7 }, (_, i) => {
    const d = new Date(this.today);
    d.setDate(this.today.getDate() + i);
    return d;
  });

  selectedDate = signal<Date>(this.today);
  selectedSlot = signal<SlotKey | null>(null);
  bookingLoading = signal(false);
  bookingSuccess = signal(false);

  readonly hours = HOURS;

  // Map: courtId → (startHour → SlotEntry), preferring DOUBLES
  slotMap = computed<Map<string, Map<number, SlotEntry>>>(() => {
    const map = new Map<string, Map<number, SlotEntry>>();
    for (const slot of this.slots()) {
      const hour = parseInt(slot.startTime.split(':')[0], 10);
      let courtMap = map.get(slot.courtId);
      if (!courtMap) {
        courtMap = new Map<number, SlotEntry>();
        map.set(slot.courtId, courtMap);
      }
      const existing = courtMap.get(hour);
      if (!existing || slot.gameMode === 'DOUBLES') {
        courtMap.set(hour, slot);
      }
    }
    return map;
  });

  selectedCourtName = computed(() => {
    const s = this.selectedSlot();
    if (!s || !this.club()) return '';
    return this.club()!.courts.find(c => c.id === s.courtId)?.name ?? '';
  });

  selectedSlotLabel = computed(() => {
    const s = this.selectedSlot();
    if (!s) return '';
    return `${s.entry.startTime} – ${s.entry.endTime}`;
  });

  slotPrice = computed(() => this.selectedSlot()?.entry.price ?? 0);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.clubService.getById(id).subscribe({
      next: club => {
        this.club.set(club);
        this.loading.set(false);
        this.loadSlots(id, this.selectedDate());
      },
      error: () => {
        this.error.set('No pudimos cargar el club');
        this.loading.set(false);
      }
    });
  }

  private loadSlots(clubId: string, date: Date): void {
    this.slotsLoading.set(true);
    this.clubService.getSlots(clubId, date).subscribe({
      next: slots => {
        this.slots.set(slots);
        this.slotsLoading.set(false);
      },
      error: () => {
        this.slots.set([]);
        this.slotsLoading.set(false);
      }
    });
  }

  selectDate(date: Date): void {
    this.selectedDate.set(date);
    this.selectedSlot.set(null);
    const id = this.route.snapshot.paramMap.get('id')!;
    this.loadSlots(id, date);
  }

  isSelected(courtId: string, hour: number): boolean {
    const s = this.selectedSlot();
    return s?.courtId === courtId && s?.hour === hour;
  }

  selectSlot(court: CourtSummary, hour: number, entry: SlotEntry): void {
    const current = this.selectedSlot();
    if (current?.courtId === court.id && current?.hour === hour) {
      this.selectedSlot.set(null);
    } else {
      this.selectedSlot.set({ courtId: court.id, hour, entry });
    }
  }

  formatDate(d: Date): { day: string; weekday: string; month: string } {
    return {
      day: String(d.getDate()).padStart(2, '0'),
      weekday: d.toLocaleDateString('es-ES', { weekday: 'short' }).toUpperCase(),
      month: d.toLocaleDateString('es-ES', { month: 'short' }).toUpperCase()
    };
  }

  isSameDay(a: Date, b: Date): boolean {
    return a.getFullYear() === b.getFullYear() &&
           a.getMonth() === b.getMonth() &&
           a.getDate() === b.getDate();
  }

  goBack(): void {
    this.router.navigate(['/clubs']);
  }

  logout(): void {
    this.authService.logout();
  }

  confirmBooking(): void {
    const slot = this.selectedSlot();
    if (!slot || this.bookingLoading()) return;

    const date = this.selectedDate();
    const start = new Date(date);
    start.setHours(slot.hour, 0, 0, 0);
    const end = new Date(date);
    end.setHours(slot.hour + 1, 0, 0, 0);

    this.bookingLoading.set(true);

    this.bookingService.createBooking({
      courtId: slot.courtId,
      startTime: start.toISOString(),
      endTime: end.toISOString(),
      gameMode: slot.entry.gameMode,
      totalPrice: slot.entry.price
    }).subscribe({
      next: () => {
        this.bookingSuccess.set(true);
        this.bookingLoading.set(false);
        this.selectedSlot.set(null);
      },
      error: () => {
        this.bookingLoading.set(false);
      }
    });
  }
}
