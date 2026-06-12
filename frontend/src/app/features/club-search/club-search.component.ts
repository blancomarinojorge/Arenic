import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs';
import { ClubService } from '../../core/clubs/club.service';
import { ClubSearchResponse, ClubSummary } from '../../core/clubs/club.models';
import { TextInputComponent } from '../../shared/components/forms/text-input-component/text-input.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-club-search',
  standalone: true,
  imports: [TextInputComponent, FormsModule],
  templateUrl: './club-search.component.html',
  styleUrl: './club-search.component.css'
})
export class ClubSearchComponent implements OnInit, OnDestroy {
  private clubService = inject(ClubService);
  private router = inject(Router);
  private destroy$ = new Subject<void>();
  private search$ = new Subject<string>();

  query = signal('');
  results = signal<ClubSearchResponse | null>(null);
  loading = signal(false);
  hasSearched = signal(false);

  ngOnInit(): void {
    this.search$.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      switchMap(q => {
        this.loading.set(true);
        this.hasSearched.set(true);
        return this.clubService.search(q);
      }),
      takeUntil(this.destroy$)
    ).subscribe({
      next: res => {
        this.results.set(res);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  onQueryChange(value: string): void {
    this.query.set(value);
    if (value.trim().length >= 2) {
      this.search$.next(value.trim());
    } else {
      this.results.set(null);
      this.hasSearched.set(false);
    }
  }

  filterByCity(city: string): void {
    this.query.set(city);
    this.search$.next(city);
  }

  goToClub(club: ClubSummary): void {
    this.router.navigate(['/clubs', club.id]);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
