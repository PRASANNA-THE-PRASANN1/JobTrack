import React, { useState } from 'react';
import { Clock, Send, CheckCircle2, Calendar, FileText, Award, AlertCircle } from 'lucide-react';
import { formatRelativeTime } from '../../utils/formatters';

export default function TimelineView({ events, onAddNote, loading }) {
  const [noteTitle, setNoteTitle] = useState('');
  const [noteDescription, setNoteDescription] = useState('');

  const handleNoteSubmit = (e) => {
    e.preventDefault();
    if (!noteTitle.trim()) return;
    onAddNote({
      eventType: 'NOTE_ADDED',
      title: noteTitle.trim(),
      description: noteDescription.trim() || undefined
    });
    setNoteTitle('');
    setNoteDescription('');
  };

  const getEventIcon = (type) => {
    switch (type) {
      case 'APPLICATION_CREATED':
        return <Send className="h-4 w-4 text-blue-600" />;
      case 'STATUS_CHANGED':
        return <CheckCircle2 className="h-4 w-4 text-indigo-600" />;
      case 'INTERVIEW_SCHEDULED':
        return <Calendar className="h-4 w-4 text-amber-600" />;
      case 'INTERVIEW_COMPLETED':
        return <Award className="h-4 w-4 text-emerald-600" />;
      case 'DOCUMENT_ATTACHED':
        return <FileText className="h-4 w-4 text-purple-600" />;
      default:
        return <Clock className="h-4 w-4 text-slate-500" />;
    }
  };

  const getEventBadgeColor = (type) => {
    switch (type) {
      case 'APPLICATION_CREATED':
        return 'bg-blue-50 border-blue-200';
      case 'STATUS_CHANGED':
        return 'bg-indigo-50 border-indigo-200';
      case 'INTERVIEW_SCHEDULED':
        return 'bg-amber-50 border-amber-200';
      case 'INTERVIEW_COMPLETED':
        return 'bg-emerald-50 border-emerald-200';
      case 'DOCUMENT_ATTACHED':
        return 'bg-purple-50 border-purple-200';
      default:
        return 'bg-slate-50 border-slate-200';
    }
  };

  return (
    <div className="space-y-6">
      {/* Quick Add Note Form */}
      <form onSubmit={handleNoteSubmit} className="bg-slate-50 p-4 rounded-xl border border-slate-200 space-y-3">
        <span className="block text-xs font-semibold text-slate-700 uppercase tracking-wider">
          Log Timeline Activity / Follow-up Note
        </span>
        <input
          type="text"
          value={noteTitle}
          onChange={(e) => setNoteTitle(e.target.value)}
          placeholder="e.g. Sent thank-you email, Recruiter called with feedback..."
          className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
        />
        <div className="flex items-center space-x-2">
          <input
            type="text"
            value={noteDescription}
            onChange={(e) => setNoteDescription(e.target.value)}
            placeholder="Additional details (optional)..."
            className="flex-1 px-3 py-1.5 text-xs rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
          />
          <button
            type="submit"
            disabled={!noteTitle.trim() || loading}
            className="px-3 py-1.5 text-xs font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm disabled:opacity-50 transition-colors"
          >
            Post
          </button>
        </div>
      </form>

      {/* Chronological Event Feed */}
      <div className="relative pl-6 space-y-6 before:absolute before:left-2.5 before:top-3 before:bottom-3 before:w-0.5 before:bg-slate-200">
        {(!events || events.length === 0) ? (
          <p className="text-xs text-slate-400 italic">No timeline entries recorded yet.</p>
        ) : (
          events.map((evt) => (
            <div key={evt.id} className="relative group">
              <div
                className={`absolute -left-6 top-1 h-5 w-5 rounded-full border flex items-center justify-center shadow-xs ${getEventBadgeColor(
                  evt.eventType
                )}`}
              >
                {getEventIcon(evt.eventType)}
              </div>
              <div className="bg-white p-3.5 rounded-xl border border-slate-200 shadow-xs hover:shadow-sm transition-all">
                <div className="flex items-center justify-between">
                  <h4 className="text-sm font-semibold text-slate-900">{evt.title}</h4>
                  <span className="text-[11px] text-slate-400 font-medium">
                    {formatRelativeTime(evt.eventDate || evt.createdAt)}
                  </span>
                </div>
                {evt.description && (
                  <p className="text-xs text-slate-600 mt-1 leading-relaxed">{evt.description}</p>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
