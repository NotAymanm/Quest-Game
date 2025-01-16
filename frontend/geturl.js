// geturl.js
window.apiBaseUrl = "http://localhost:8080"; // Fallback value

// Check if the script is executing
console.log('geturl.js loaded');
// console.log('Initial apiBaseUrl:', window.apiBaseUrl);

if (import.meta.env && import.meta.env.VITE_API_URL) {
    window.apiBaseUrl = import.meta.env.VITE_API_URL;
    // console.log('apiBaseUrl set from environment:', window.apiBaseUrl);
} else {
    // console.log('Using fallback apiBaseUrl:', window.apiBaseUrl);
}
