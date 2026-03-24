const headerMapping = {
  username: 'Username',
  total_wins: 'Total Wins',
  total_losses: 'Total Losses',
  total_points: 'Total Points'
};

const fieldsToDisplay = ['username', 'total_wins', 'total_losses', 'total_points'];

fetch('../js/game.csv')
  .then(response => response.text())
  .then(data => {
    const rows = data.trim().split('\n').map(row => row.split(','));
    const table = document.getElementById('leaderboard-table');

    const headers = rows[0];

    const fieldIndexMap = {};
    headers.forEach((header, idx) => {
      fieldIndexMap[header.trim()] = idx;
    });

    // parse all players into a player array
    const players = [];

    for (let i = 1; i < rows.length; i++) {
      const player = {};
      fieldsToDisplay.forEach(field => {
        const idx = fieldIndexMap[field];
        player[field] = rows[i][idx]?.trim() || '';
      });

      // win percentage
      const totalWins = parseInt(player.total_wins);
      const totalLosses = parseInt(player.total_losses);
      player.winPercentage = (totalWins + totalLosses) > 0
        ? (totalWins / (totalWins + totalLosses)) * 100
        : 0;

      player.total_points = parseInt(player.total_points);

      players.push(player);
    }

    // sort players by points
    players.sort((a, b) => b.total_points - a.total_points);

    // table header
    const headerRow = document.createElement('tr');
    fieldsToDisplay.forEach(field => {
      const th = document.createElement('th');
      th.textContent = headerMapping[field] || field;
      headerRow.appendChild(th);
    });

    const winPercHeader = document.createElement('th');
    winPercHeader.textContent = 'Win Percentage';
    headerRow.appendChild(winPercHeader);

    table.appendChild(headerRow);

    // creates table rows for each sorted player
    players.forEach((player, index) => {
      const tr = document.createElement('tr');
    
      fieldsToDisplay.forEach(field => {
        const td = document.createElement('td');
    
        if (field === 'username' && index === 0) {
          td.textContent = '🏆 ' + player[field];
        } else {
          td.textContent = player[field];
        }
    
        tr.appendChild(td);
      });
    
      const winPercCell = document.createElement('td');
      winPercCell.textContent = player.winPercentage.toFixed(2) + '%';
      tr.appendChild(winPercCell);
    
      table.appendChild(tr);
    });
  });
