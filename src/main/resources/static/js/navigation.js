// Enhanced Tab Navigation Script
document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.nav-tab:not(.disabled)');

    // ── Hamburger menu toggle ────────────────────────────────────────────
    const hamburger = document.getElementById('navHamburger');
    const navTabs   = document.getElementById('navTabs');

    if (hamburger && navTabs) {
        hamburger.addEventListener('click', function() {
            const isOpen = navTabs.classList.toggle('open');
            hamburger.setAttribute('aria-expanded', isOpen);
        });

        // Close menu when a nav link is tapped on mobile
        navTabs.querySelectorAll('.nav-tab:not(.disabled)').forEach(tab => {
            tab.addEventListener('click', () => {
                navTabs.classList.remove('open');
                hamburger.setAttribute('aria-expanded', 'false');
            });
        });

        // Close menu when tapping outside
        document.addEventListener('click', function(e) {
            if (!hamburger.contains(e.target) && !navTabs.contains(e.target)) {
                navTabs.classList.remove('open');
                hamburger.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // ── Click animation ──────────────────────────────────────────────────
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            this.style.transform = 'translateY(1px)';
            setTimeout(() => { this.style.transform = ''; }, 150);
        });
    });

    // ── Disabled tab shake ───────────────────────────────────────────────
    document.querySelectorAll('.nav-tab.disabled').forEach(tab => {
        tab.addEventListener('click', function(e) {
            e.preventDefault();
            this.style.animation = 'shake 0.5s';
            setTimeout(() => { this.style.animation = ''; }, 500);
        });
    });
});

// Injected styles for animations
const style = document.createElement('style');
style.textContent = `
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        10%, 30%, 50%, 70%, 90% { transform: translateX(-2px); }
        20%, 40%, 60%, 80% { transform: translateX(2px); }
    }
    .nav-tab:active:not(.disabled) {
        transform: translateY(1px) !important;
    }
`;
document.head.appendChild(style);
