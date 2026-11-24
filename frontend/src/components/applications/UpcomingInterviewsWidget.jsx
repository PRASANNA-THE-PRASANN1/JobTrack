import React from 'react';
import { Calendar, Clock, Video, ChevronRight, User } from 'lucide-react';
import { formatDate } from '../../utils/formatters';

export default function UpcomingInterviewsWidget({ interviews, onSelectInterview }) {
  if (!interviews || interviews.length === 0) {
    return null;
  }

  const formatInterviewDateTime = (dateStr) => {
    try {
      const d = new Date(dateStr);
      return d.toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
      });
    } catch {
      return dateStr;
    }
  };

  return (
    <div className="bg-gradient-to-r from-indigo-900 via-indigo-800 to-slate-900 rounded-2xl p-6 text-white shadow-lg mb-8">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center space-x-2.5">
          <div className="h-8 w-8 rounded-lg bg-indigo-500/20 border border-indigo-400/30 flex items-center justify-center text-indigo-300">
            <Calendar className="h-4 w-4" />
          </div>
          <div>
            <h3 className="font-semibold text-base text-white">Upcoming Interviews</h3>
            <p className="text-xs text-indigo-200">Scheduled rounds across your active pipeline</p>
          </div>
        </div>
        <span className="text-xs font-semibold px-2.5 py-1 rounded-full bg-indigo-500/30 text-indigo-200 border border-indigo-400/20">
          {interviews.length} Scheduled
        </span>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
        {interviews.slice(0, 3).map((item) => (
          <div
            key={item.id}
            onClick={() => onSelectInterview(item.applicationId)}
            className="bg-white/10 hover:bg-white/15 backdrop-blur-md rounded-xl p-4 border border-white/10 cursor-pointer transition-all duration-150 group"
          >
            <div className="flex items-start justify-between">
              <div>
                <span className="text-xs font-bold text-indigo-300 uppercase tracking-wide">
                  {item.companyName}
                </span>
                <h4 className="text-sm font-semibold text-white group-hover:text-indigo-200 transition-colors">
                  {item.roundName}
                </h4>
              </div>
              <span className="text-[10px] font-semibold px-2 py-0.5 rounded-full bg-indigo-400/20 text-indigo-200">
                {item.roundType?.replace('_', ' ')}
              </span>
            </div>

            <div className="mt-3 flex items-center space-x-3 text-xs text-indigo-100">
              <div className="flex items-center space-x-1">
                <Clock className="h-3.5 w-3.5 text-indigo-300" />
                <span>{formatInterviewDateTime(item.interviewDate)}</span>
              </div>
              <span>•</span>
              <span>{item.durationMinutes} min</span>
            </div>

            <div className="mt-3 pt-3 border-t border-white/10 flex items-center justify-between text-xs">
              {item.meetingLink ? (
                <a
                  href={item.meetingLink}
                  target="_blank"
                  rel="noopener noreferrer"
                  onClick={(e) => e.stopPropagation()}
                  className="inline-flex items-center space-x-1 text-emerald-300 hover:text-emerald-200 font-medium"
                >
                  <Video className="h-3.5 w-3.5" />
                  <span>Join Meeting</span>
                </a>
              ) : (
                <span className="text-white/40">No link added</span>
              )}

              <span className="flex items-center space-x-1 text-indigo-200 group-hover:translate-x-0.5 transition-transform">
                <span>View App</span>
                <ChevronRight className="h-3.5 w-3.5" />
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
