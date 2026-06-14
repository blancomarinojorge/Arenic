import {Component, Input} from '@angular/core';

export type AppIconName = 'email' | 'lock' | 'search' | 'location' | 'court' | 'logout';

@Component({
  selector: 'app-app-icon',
  standalone: true,
  imports: [],
  template: `
    <div [style.width]="width">
      @switch (icon){
        @case ('lock') { <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M5 11m0 2a2 2 0 0 1 2 -2h10a2 2 0 0 1 2 2v6a2 2 0 0 1 -2 2h-10a2 2 0 0 1 -2 -2z" /><path d="M12 16m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0" /><path d="M8 11v-4a4 4 0 1 1 8 0v4" /></svg> }
        @case ('email') { <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 7a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v10a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2z" /><path d="M3 7l9 6l9 -6" /></svg> }
        @case ('search') { <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M10 10m-7 0a7 7 0 1 0 14 0a7 7 0 1 0 -14 0" /><path d="M21 21l-6 -6" /></svg> }
        @case ('location') { <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M9 11a3 3 0 1 0 6 0a3 3 0 0 0 -6 0" /><path d="M17.657 16.657l-4.243 4.243a2 2 0 0 1 -2.827 0l-4.244 -4.243a8 8 0 1 1 11.314 0z" /></svg> }
        @case ('court') { <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 5m0 2a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v10a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2z" /><path d="M12 5v14" /><path d="M3 12h18" /></svg> }
        @case ('logout') { <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M17 16l4-4m0 0l-4-4m4 4H9m3-7H5a2 2 0 00-2 2v10a2 2 0 002 2h7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg> }
      }
    </div>
  `,
  styles: [`:host { display: inline-flex; } svg{ width: 100%; height: 100% }`]
})
export class AppIconComponent {
  @Input() icon!: AppIconName;

  /**
   * Width of the icon. Can be passed as:
   * - Plain pixel values: width="24px"
   * - Rem values: width="1.5rem"
   * - CSS variables: width="var(--icon-size)"
   * - Design system tokens: width="var(--spacing-l)"
   * Default: 1.5rem
   *
   * Examples:
   * <app-app-icon icon="email" width="2rem"></app-app-icon>
   * <app-app-icon icon="lock" width="24px"></app-app-icon>
   * <app-app-icon icon="search" width="var(--my-icon-size)"></app-app-icon>
   */
  @Input() width: string = 'var(--icon-size-m)';
}
