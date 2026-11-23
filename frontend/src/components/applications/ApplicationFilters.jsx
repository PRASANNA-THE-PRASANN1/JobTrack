import React from 'react';
import { Search, Filter, ArrowUpDown, X } from 'lucide-react';

export default function ApplicationFilters({
  search,
  onSearchChange,
  status,
  onStatusChange,
  workMode,
  onWorkModeChange,
  sortBy,
  onSortByChange,
  direction,
  onDirectionChange,
  onReset
}) {
  const hasActiveFilters = search || status || workMode || sortBy !== 'applicationDate' || direction !== 'desc';

  return (
    <div className="bg-white rounded-xl border border-slate-200 p-4 shadow-sm mb-6 space-y-4">
      <div className="flex flex-col lg:flex-row gap-3 items-stretch lg:items-center justify-between">
        {/* Search Input */}
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-400" />
          <input
            type="text"
            value={search}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder="Search company, job title, or location..."
            className="w-full pl-9 pr-8 py-2 text-sm bg-slate-50 border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:bg-white transition-all"
          />
          {search && (
            <button
              onClick={() => onSearchChange('')}
              className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
            >
              <X className="h-3.5 w-3.5" />
            </button>
          )}
        </div>

        {/* Filter Dropdowns */}
        <div className="flex flex-wrap items-center gap-2">
          {/* Status Filter */}
          <select
            value={status}
            onChange={(e) => onStatusChange(e.target.value)}
            className="px-3 py-2 text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">All Statuses</option>
            <option value="SAVED">Saved</option>
            <option value="APPLIED">Applied</option>
            <option value="SCREENING">Screening</option>
            <option value="INTERVIEWING">Interviewing</option>
            <option value="OFFERED">Offered</option>
            <option value="REJECTED">Rejected</option>
            <option value="WITHDRAWN">Withdrawn</option>
          </select>

          {/* Work Mode Filter */}
          <select
            value={workMode}
            onChange={(e) => onWorkModeChange(e.target.value)}
            className="px-3 py-2 text-xs font-medium bg-slate-50 border border-slate-200 rounded-lg text-slate-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">All Work Modes</option>
            <option value="REMOTE">Remote</option>
            <option value="HYBRID">Hybrid</option>
            <option value="ONSITE">Onsite</option>
          </select>

          {/* Sort By */}
          <div className="flex items-center space-x-1 bg-slate-50 border border-slate-200 rounded-lg p-0.5">
            <select
              value={sortBy}
              onChange={(e) => onSortByChange(e.target.value)}
              className="px-2.5 py-1.5 text-xs font-medium bg-transparent text-slate-700 focus:outline-none"
            >
              <option value="applicationDate">Date Applied</option>
              <option value="companyName">Company</option>
              <option value="jobTitle">Job Title</option>
              <option value="status">Status</option>
            </select>
            <button
              onClick={() => onDirectionChange(direction === 'asc' ? 'desc' : 'asc')}
              title={`Sorting ${direction === 'asc' ? 'Ascending' : 'Descending'}`}
              className="p-1.5 text-slate-600 hover:text-indigo-600 rounded hover:bg-slate-200/60 transition-colors"
            >
              <ArrowUpDown className="h-3.5 w-3.5" />
            </button>
          </div>

          {/* Clear Filters Button */}
          {hasActiveFilters && (
            <button
              onClick={onReset}
              className="px-3 py-2 text-xs font-medium text-slate-500 hover:text-slate-800 hover:bg-slate-100 rounded-lg transition-colors border border-dashed border-slate-300"
            >
              Reset
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
