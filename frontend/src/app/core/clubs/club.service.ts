import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClubSearchResponse, ClubDetail, SlotEntry } from './club.models';
import { enviroment } from '../../../enviroments/enviroment';

@Injectable({ providedIn: 'root' })
export class ClubService {
  private http = inject(HttpClient);
  private base = enviroment.apiUrl;

  search(query: string): Observable<ClubSearchResponse> {
    const params = new HttpParams().set('query', query);
    return this.http.get<ClubSearchResponse>(`${this.base}/clubs/search`, { params });
  }

  getById(id: string): Observable<ClubDetail> {
    return this.http.get<ClubDetail>(`${this.base}/clubs/${id}`);
  }

  getSlots(clubId: string, date: Date): Observable<SlotEntry[]> {
    const dateStr = date.toISOString().split('T')[0];
    const params = new HttpParams().set('date', dateStr);
    return this.http.get<SlotEntry[]>(`${this.base}/clubs/${clubId}/slots`, { params });
  }
}
