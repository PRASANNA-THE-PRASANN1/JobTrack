export const formatCurrency = (amount, currency = 'USD') => {
  if (amount == null) return null;
  try {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency || 'USD',
      maximumFractionDigits: 0
    }).format(amount);
  } catch (e) {
    return `${currency} ${amount}`;
  }
};

export const formatDate = (dateString) => {
  if (!dateString) return '—';
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  } catch (e) {
    return dateString;
  }
};

export const formatRelativeTime = (timestamp) => {
  if (!timestamp) return '';
  try {
    const now = new Date();
    const then = new Date(timestamp);
    const diffDays = Math.floor((now - then) / (1000 * 60 * 60 * 24));
    if (diffDays === 0) return 'Today';
    if (diffDays === 1) return 'Yesterday';
    if (diffDays < 30) return `${diffDays}d ago`;
    return formatDate(timestamp);
  } catch (e) {
    return '';
  }
};
