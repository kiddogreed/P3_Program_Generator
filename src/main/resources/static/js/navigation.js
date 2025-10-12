// Enhanced Tab Navigation Script
document.addEventListener('DOMContentLoaded', function() {
    const tabs = document.querySelectorAll('.nav-tab:not(.disabled)');
    const currentPath = window.location.pathname;
    
    // Add click animation to tabs
    tabs.forEach(tab => {
        tab.addEventListener('click', function(e) {
            // Don't prevent default - let the navigation happen
            
            // Add visual feedback
            this.style.transform = 'translateY(1px)';
            setTimeout(() => {
                this.style.transform = '';
            }, 150);
        });
        
        // Add hover sound effect (optional)
        tab.addEventListener('mouseenter', function() {
            this.style.transition = 'all 0.2s ease';
        });
    });
    
    // Handle disabled tab clicks
    const disabledTabs = document.querySelectorAll('.nav-tab.disabled');
    disabledTabs.forEach(tab => {
        tab.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Shake animation for disabled tabs
            this.style.animation = 'shake 0.5s';
            setTimeout(() => {
                this.style.animation = '';
            }, 500);
        });
    });
});

// Shake animation for disabled tabs
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