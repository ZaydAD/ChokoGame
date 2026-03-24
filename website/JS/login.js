import { csvToJson } from './csvToJson.js';

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('login-form');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
      const response = await fetch('../js/users.csv');
      const csv = await response.text();
      const users = csvToJson(csv);

      const match = users.find(user =>
        user.username === username && user.password === password
      );

      if (match) {
        const userData = {
          username: match.username,
          total_points: match.total_points,
          total_wins: match.total_wins,
          total_losses: match.total_losses
        };

        localStorage.setItem('loggedInUserData', JSON.stringify(userData));
        console.log("Redirecting to statistics page...");
        window.location.href = '../html/statistics.html';
      } else {
        alert('Invalid credentials.');
      }

    } catch (err) {
      console.error('Error loading CSV:', err);
      alert('Could not load user data.');
    }
  });
});
