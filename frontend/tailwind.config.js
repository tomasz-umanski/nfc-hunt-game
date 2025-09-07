/** @type {import('tailwindcss').Config} */
export default {
    content: ['./src/**/*.{js,jsx,ts,tsx}'],
    theme: {
        extend: {
            fontFamily: {
                crimson: ['Crimson Pro, serif'],
            },
            colors: {
                primary: {
                    500: '#F1E6C4',
                },
            },
        },
    },
    plugins: [],
}