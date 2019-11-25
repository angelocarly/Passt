
document.addEventListener('DOMContentLoaded', function () {
  document.getElementById('buttonLogout').addEventListener('click', function click() {
      window.location.href = '/client/logout'
  })
});

document.addEventListener('DOMContentLoaded', function () {
  document.getElementById('buttonTokens').addEventListener('click', function click() {
      window.location.href = '/client/tokens'
  })
});