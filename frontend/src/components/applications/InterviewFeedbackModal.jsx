import React, { useState } from 'react';
import Modal from '../common/Modal';
import { Star } from 'lucide-react';

export default function InterviewFeedbackModal({ isOpen, onClose, interview, onSaveFeedback }) {
  if (!interview) return null;

  const [status, setStatus] = useState(interview.status || 'COMPLETED');
  const [rating, setRating] = useState(interview.rating || 4);
  const [feedbackNotes, setFeedbackNotes] = useState(interview.feedbackNotes || '');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSaveFeedback(interview.id, {
      ...interview,
      status,
      rating,
      feedbackNotes
    });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`Interview Feedback — ${interview.roundName}`} maxWidth="max-w-md">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Status
          </label>
          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
          >
            <option value="COMPLETED">Completed</option>
            <option value="CANCELLED">Cancelled</option>
            <option value="RESCHEDULED">Rescheduled</option>
            <option value="SCHEDULED">Scheduled</option>
          </select>
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Self-Assessment Rating
          </label>
          <div className="flex items-center space-x-1.5 py-1">
            {[1, 2, 3, 4, 5].map((star) => (
              <button
                type="button"
                key={star}
                onClick={() => setRating(star)}
                className="p-1 text-slate-300 hover:text-amber-400 focus:outline-none transition-colors"
              >
                <Star
                  className={`h-6 w-6 ${
                    star <= rating ? 'text-amber-400 fill-amber-400' : 'text-slate-200'
                  }`}
                />
              </button>
            ))}
            <span className="text-xs text-slate-500 font-medium ml-2">
              {rating === 5 ? 'Exceptional' : rating === 4 ? 'Solid Performance' : rating === 3 ? 'Neutral' : 'Needs Work'}
            </span>
          </div>
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
            Feedback & Questions Asked
          </label>
          <textarea
            rows="4"
            value={feedbackNotes}
            onChange={(e) => setFeedbackNotes(e.target.value)}
            placeholder="What technical questions were asked? What went well, what could be improved?"
            className="w-full px-3 py-2 text-sm rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
        </div>

        <div className="flex items-center justify-end space-x-3 pt-3 border-t border-slate-100">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="px-5 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg shadow-sm"
          >
            Save Feedback
          </button>
        </div>
      </form>
    </Modal>
  );
}
