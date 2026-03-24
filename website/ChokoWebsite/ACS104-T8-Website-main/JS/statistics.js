document.addEventListener('DOMContentLoaded', () => {
    const userData = JSON.parse(localStorage.getItem('loggedInUserData'));

    if (!userData) {
        alert("Not logged in.");
        window.location.href = '../login.html';
        return;
    }

    document.getElementById('hello').textContent = `${userData.username}`;
    document.getElementById('totalPoints').textContent = `${userData.total_points}`;
    document.getElementById('totalWins').textContent = `${userData.total_wins}`;
    document.getElementById('totalLosses').textContent = `${userData.total_losses}`;

    const wins = Number(userData.total_wins);
    const losses = Number(userData.total_losses);
    const totalGames = wins + losses;

    console.log(wins + " " + losses + " " + totalGames);

    let winPercentage = "N/A";
    if (totalGames > 0) {
        winPercentage = `${((wins / totalGames) * 100).toFixed(2)}%`;
    }

    document.getElementById('winPercentage').textContent = winPercentage;
});
