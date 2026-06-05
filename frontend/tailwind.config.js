/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}"
  ],
  /*theme: {
    extend: {},
  },*/
  plugins: [],
  theme: {
    extend: {
      "colors": {
        "surface-bright": "#fcf9f8",
        "on-surface": "#1c1b1b",
        "outline": "#737781",
        "on-secondary-container": "#5e0500",
        "tertiary-container": "#3f4f5c",
        "inverse-on-surface": "#f3f0ef",
        "outline-variant": "#c3c6d1",
        "on-primary-fixed-variant": "#194780",
        "secondary": "#b42815",
        "surface-dim": "#dcd9d9",
        "on-tertiary": "#ffffff",
        "on-background": "#1c1b1b",
        "background": "#fcf9f8",
        "surface-container-highest": "#e5e2e1",
        "on-tertiary-fixed": "#0c1d29",
        "on-secondary-fixed": "#3f0200",
        "on-primary-fixed": "#001b3c",
        "surface-container-low": "#f6f3f2",
        "tertiary-fixed": "#d3e5f5",
        "on-error-container": "#93000a",
        "surface-variant": "#e5e2e1",
        "on-secondary-fixed-variant": "#900d00",
        "surface-container-lowest": "#ffffff",
        "inverse-primary": "#a8c8ff",
        "primary-fixed-dim": "#a8c8ff",
        "on-primary-container": "#99bfff",
        "secondary-container": "#fd5d44",
        "on-tertiary-container": "#afc0d0",
        "primary-container": "#214d86",
        "inverse-surface": "#313030",
        "error": "#ba1a1a",
        "primary": "#00366b",
        "surface-container": "#f0eded",
        "surface": "#fcf9f8",
        "on-error": "#ffffff",
        "tertiary": "#283844",
        "error-container": "#ffdad6",
        "primary-fixed": "#d5e3ff",
        "tertiary-fixed-dim": "#b7c9d8",
        "surface-tint": "#365f99",
        "secondary-fixed-dim": "#ffb4a6",
        "on-secondary": "#ffffff",
        "on-surface-variant": "#434750",
        "on-primary": "#ffffff",
        "surface-container-high": "#eae7e7",
        "on-tertiary-fixed-variant": "#394955",
        "secondary-fixed": "#ffdad4"
      },
      "borderRadius": {
        "DEFAULT": "0.125rem",
        "lg": "0.25rem",
        "xl": "0.5rem",
        "full": "0.75rem"
      },
      "spacing": {
        "unit": "4px",
        "gutter": "24px",
        "margin-mobile": "16px",
        "margin-desktop": "64px",
        "container-max": "1280px"
      },
      "fontFamily": {
        "body-md": ["Manrope"],
        "label-lg": ["Manrope"],
        "headline-md": ["Chivo"],
        "headline-lg-mobile": ["Chivo"],
        "label-md": ["Manrope"],
        "headline-sm": ["Chivo"],
        "body-lg": ["Manrope"],
        "headline-lg": ["Chivo"]
      },
      "fontSize": {
        "body-md": ["16px", {"lineHeight": "1.5", "fontWeight": "400"}],
        "label-lg": ["14px", {"lineHeight": "1.2", "fontWeight": "600"}],
        "headline-md": ["32px", {"lineHeight": "1.2", "fontWeight": "800"}],
        "headline-lg-mobile": ["32px", {"lineHeight": "1.1", "fontWeight": "800"}],
        "label-md": ["12px", {"lineHeight": "1.2", "fontWeight": "500"}],
        "headline-sm": ["24px", {"lineHeight": "1.2", "fontWeight": "700"}],
        "body-lg": ["18px", {"lineHeight": "1.6", "fontWeight": "400"}],
        "headline-lg": ["48px", {"lineHeight": "1.1", "letterSpacing": "-0.02em", "fontWeight": "800"}]
      }
    },
  }
}

