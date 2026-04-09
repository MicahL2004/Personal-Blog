/**
 * Personal Blog – minimal JavaScript helpers.
 */

// Ask for confirmation before deleting a post.
function confirmDelete(postTitle) {
    return window.confirm('Are you sure you want to delete "' + postTitle + '"?\nThis action cannot be undone.');
}

// Auto-hide flash messages after 5 seconds.
document.addEventListener('DOMContentLoaded', function () {
    const flash = document.querySelector('.flash');
    if (flash) {
        setTimeout(function () {
            flash.style.transition = 'opacity 0.6s';
            flash.style.opacity = '0';
            setTimeout(function () { flash.remove(); }, 600);
        }, 5000);
    }
});
