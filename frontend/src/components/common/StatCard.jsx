import React from 'react';

export default function StatCard({ title, count, icon: Icon, color, subtext, active }) {
  const colorMap = {
    indigo: {
      bg: 'bg-indigo-50',
      text: 'text-indigo-600',
      border: 'border-indigo-100',
      ring: 'ring-indigo-500/20',
    },
    blue: {
      bg: 'bg-blue-50',
      text: 'text-blue-600',
      border: 'border-blue-100',
      ring: 'ring-blue-500/20',
    },
    amber: {
      bg: 'bg-amber-50',
      text: 'text-amber-600',
      border: 'border-amber-100',
      ring: 'ring-amber-500/20',
    },
    emerald: {
      bg: 'bg-emerald-50',
      text: 'text-emerald-600',
      border: 'border-emerald-100',
      ring: 'ring-emerald-500/20',
    },
    rose: {
      bg: 'bg-rose-50',
      text: 'text-rose-600',
      border: 'border-rose-100',
      ring: 'ring-rose-500/20',
    },
    slate: {
      bg: 'bg-slate-50',
      text: 'text-slate-600',
      border: 'border-slate-100',
      ring: 'ring-slate-500/20',
    }
  };

  const scheme = colorMap[color] || colorMap.indigo;

  return (
    <div className={`bg-white rounded-xl border border-slate-200/80 p-5 shadow-sm transition-all duration-150 hover:shadow-md ${active ? 'ring-2 ' + scheme.ring : ''}`}>
      <div className="flex items-center justify-between">
        <div>
          <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">{title}</p>
          <p className="text-2xl font-bold text-slate-900 mt-1">{count ?? 0}</p>
        </div>
        <div className={`p-3 rounded-xl ${scheme.bg} ${scheme.text}`}>
          <Icon className="h-6 w-6" />
        </div>
      </div>
      {subtext && (
        <p className="text-xs text-slate-500 mt-3 pt-2 border-t border-slate-100">{subtext}</p>
      )}
    </div>
  );
}
