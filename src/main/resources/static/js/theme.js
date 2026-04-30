(function () {
    var STORAGE_KEY = 'wgr-theme';

    function getPreferredTheme() {
        var stored = null;
        try { stored = localStorage.getItem(STORAGE_KEY); } catch (e) { /* ignore */ }
        if (stored === 'light' || stored === 'dark') return stored;
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            return 'dark';
        }
        return 'light';
    }

    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        var btns = document.querySelectorAll('.theme-toggle');
        btns.forEach(function (btn) {
            var icon = btn.querySelector('i');
            if (icon) {
                icon.className = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
            }
            btn.setAttribute('aria-label',
                theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode');
            btn.setAttribute('title',
                theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode');
        });
    }

    function toggleTheme() {
        var current = document.documentElement.getAttribute('data-theme') || getPreferredTheme();
        var next = current === 'dark' ? 'light' : 'dark';
        try { localStorage.setItem(STORAGE_KEY, next); } catch (e) { /* ignore */ }
        applyTheme(next);
    }

    // Apply ASAP to avoid flash
    applyTheme(getPreferredTheme());

    document.addEventListener('DOMContentLoaded', function () {
        applyTheme(document.documentElement.getAttribute('data-theme') || getPreferredTheme());
        document.querySelectorAll('.theme-toggle').forEach(function (btn) {
            btn.addEventListener('click', function (e) {
                e.preventDefault();
                toggleTheme();
            });
        });
    });

    // Expose for inline handlers if needed
    window.WGRTheme = { toggle: toggleTheme, apply: applyTheme };
})();
