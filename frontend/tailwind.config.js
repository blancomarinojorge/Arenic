/** @type {import('tailwindcss').Config} */
module.exports = {
  /*IDE configuration so it autocompletes our custom classes*/
  safelist: [
    { pattern: /^ds-/ },
  ],
  content: [
    "./src/**/*.{html,ts}",
    "./src/styles.css"
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Raleway', 'sans-serif'],
        brand: ['"Barlow Condensed"', 'sans-serif']
      },
      fontSize: {
        // Headings
        'heading-2xs': ['0.875rem', { lineHeight: '1.25rem' }],
        'heading-xs':  ['1rem',     { lineHeight: '1.5rem' }],
        'heading-s':   ['1.25rem',  { lineHeight: '1.75rem' }],
        'heading-m':   ['1.5rem',   { lineHeight: '2rem' }],
        'heading-l':   ['2rem',     { lineHeight: '2.5rem' }],
        'heading-xl':  ['2.5rem',   { lineHeight: '3rem' }],
        'heading-2xl': ['3rem',     { lineHeight: '3.5rem' }],
        'heading-3xl': ['3.5rem',   { lineHeight: '4rem' }],
        'heading-4xl': ['4rem',     { lineHeight: '4.5rem' }],
        'heading-5xl': ['4.5rem',   { lineHeight: '5rem' }],

        // Text
        'text-xs': ['0.625rem', { lineHeight: '0.875rem' }],
        'text-s':  ['0.75rem',  { lineHeight: '1rem' }],
        'text-m':  ['0.875rem', { lineHeight: '1.25rem' }],
        'text-l':  ['1rem',     { lineHeight: '1.5rem' }],
        'text-xl': ['1.25rem',  { lineHeight: '1.75rem' }],
      },
      colors: {

        // ==========================================
        // CONTENT TOKENS (Generates: text-content-*)
        // ==========================================
        content: {
          'primary': 'var(--color-content-primary)',
          'primary-inverse': 'var(--color-content-primary-inverse)',
          'primary-hover': 'var(--color-content-primary-hover)',
          'secondary': 'var(--color-content-secondary)',
          'brand-primary': 'var(--color-content-brand-primary)',
          'brand-secondary': 'var(--color-content-brand-secondary)',
          'link': 'var(--color-content-link)',
          'link-hover': 'var(--color-content-link-hover)',
          'link-pressed': 'var(--color-content-link-pressed)',
          'negative': 'var(--color-content-negative)',
        },

        // ==========================================
        // BACKGROUND TOKENS (Generates: bg-bg-*)
        // ==========================================
        bg: {
          'primary': 'var(--color-bg-primary)',
          'primary-hover': 'var(--color-bg-primary-hover)',
          'brand': 'var(--color-bg-brand)',
          'brand-hover': 'var(--color-bg-brand-hover)',
          'brand-pressed': 'var(--color-bg-brand-pressed)',
        },

        // ==========================================
        // BORDER TOKENS (Generates: border-border-*)
        // ==========================================
        border: {
          'primary': 'var(--color-border-primary)',
          'secondary': 'var(--color-border-secondary)',
          'negative': 'var(--color-border-negative)',
          'focus': 'var(--color-border-focus)',
        },

        // ==========================================
        // SURFACE TOKENS (Generates: bg-surface-* / text-surface-*)
        // ==========================================
        surface: {
          'l1': 'var(--color-surface-l1)',
          'l2': 'var(--color-surface-l2)',
        },

        // ==========================================
        // OVERLAY TOKENS (Generates: bg-overlay-*)
        // ==========================================
        overlay: {
          '50': 'var(--color-overlay-50)',
        }
      },
      spacing: {
        '2xs':  '0.125rem',  // 2px
        'xs':   '0.25rem',   // 4px
        's':    '0.5rem',    // 8px
        'm':    '0.75rem',   // 12px
        'l':    '1rem',      // 16px
        'xl':   '1.5rem',    // 24px
        '2xl':  '2rem',      // 32px
        '3xl':  '2.5rem',    // 40px
        '4xl':  '3rem',      // 48px
        '5xl':  '3.5rem',    // 56px
        '6xl':  '4rem',      // 64px
        '7xl':  '4.5rem',    // 72px
        '8xl':  '5rem',      // 80px
        '9xl':  '5.5rem',    // 88px
        '10xl': '6.125rem',  // 98px
        '11xl': '6.5rem',    // 104px
        '12xl': '7rem',      // 112px
      },
      // ==========================================
      // BORDER RADIUS TOKENS (Generates: rounded-*)
      // ==========================================
      borderRadius: {
        'circle': '50px',
        'pill': '999px',
        'l': '16px',
        'm': '12px',
        's': '8px',
        'xs': '4px',
        '2xs': '2px',
      },

      // ==========================================
      // BORDER WIDTH TOKENS (Generates: border-*)
      // ==========================================
      borderWidth: {
        'xl': '8px',
        'l': '4px',
        'm': '2px',
        's': '1.5px',
        'xs': '1px',
      },
    },
  },
  plugins: [],

}

