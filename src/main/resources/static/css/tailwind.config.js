/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "../../templates/**/*.{html,js}",
         "../../templates/user/**/*.html",
         "./js/*.{html, js}",
         "../../templates/user/Vehicle/**/*.html",
         "../../templates/user/driver/**/*.html",
         "../../templates/admin/**/*.html",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'dots': 'radial-gradient(circle, rgba(0, 0, 0, 0.1) 1px, transparent 1px)',
      },
      backgroundSize: {
        'dots': '20px 20px',
      },
      backgroundColor: {
        'light-gray': '#f3f4f6',
      },
    },
  },
  plugins: [],
};
