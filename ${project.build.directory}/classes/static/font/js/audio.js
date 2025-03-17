 const videoContainer = document.querySelector('.video-container');
const glowEffect = document.querySelector('.glow-effect');

videoContainer.addEventListener('mouseover', () => {
  glowEffect.style.display = 'block';
});

videoContainer.addEventListener('mouseout', () => {
  glowEffect.style.display = 'none';
});

