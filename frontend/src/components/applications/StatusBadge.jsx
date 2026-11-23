import React from 'react';

const statusConfig = {
  SAVED: {
    label: 'Saved',
    classes: 'bg-slate-100 text-slate-700 border-slate-200',
    dot: 'bg-slate-400'
  },
  APPLIED: {
    label: 'Applied',
    classes: 'bg-blue-50 text-blue-700 border-blue-200/80',
    dot: 'bg-blue-500'
  },
  SCREENING: {
    label: 'Screening',
    classes: 'bg-purple-50 text-purple-700 border-purple-200/80',
    dot: 'bg-purple-500'
  },
  INTERVIEWING: {
    label: 'Interviewing',
    classes: 'bg-amber-50 text-amber-700 border-amber-200/80',
    dot: 'bg-amber-500'
  },
  OFFERED: {
    label: 'Offered',
    classes: 'bg-emerald-50 text-emerald-700 border-emerald-200/80',
    dot: 'bg-emerald-500'
  },
  REJECTED: {
    label: 'Rejected',
    classes: 'bg-rose-50 text-rose-700 border-rose-200/80',
    dot: 'bg-rose-500'
  },
  WITHDRAWN: {
    label: 'Withdrawn',
    classes: 'bg-zinc-100 text-zinc-600 border-zinc-200',
    dot: 'bg-zinc-400'
  }
};

export default function StatusBadge({ status, size = 'sm' }) {
  const config = statusConfig[status] || statusConfig.APPLIED;
  const sizeClasses = size === 'xs' ? 'text-[11px] px-2 py-0.5' : 'text-xs px-2.5 py-1';

  return (
    <span className={`inline-flex items-center space-x-1.5 font-medium rounded-full border ${config.classes} ${sizeClasses}`}>
      <span className={`h-1.5 w-1.5 rounded-full ${config.dot}`} />
      <span>{config.label}</span>
    </span>
  );
}
