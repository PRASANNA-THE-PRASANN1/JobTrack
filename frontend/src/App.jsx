import React, { useState, useEffect, useCallback } from 'react';
import Navbar from './components/common/Navbar';
import StatCard from './components/common/StatCard';
import ApplicationFilters from './components/applications/ApplicationFilters';
import ApplicationList from './components/applications/ApplicationList';
import ApplicationFormModal from './components/applications/ApplicationFormModal';
import ApplicationDetailModal from './components/applications/ApplicationDetailModal';
import ConfirmDialog from './components/common/ConfirmDialog';
import { applicationService } from './api/applicationService';
import { Briefcase, Send, Users, Award, XCircle, AlertCircle, CheckCircle2 } from 'lucide-react';

export default function App() {
  const [applications, setApplications] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const [isApiConnected, setIsApiConnected] = useState(false);
  const [toast, setToast] = useState(null);

  // Filters & Pagination State
  const [search, setSearch] = useState('');
  const [status, setStatus] = useState('');
  const [workMode, setWorkMode] = useState('');
  const [sortBy, setSortBy] = useState('applicationDate');
  const [direction, setDirection] = useState('desc');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Modals state
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingApp, setEditingApp] = useState(null);
  const [viewingApp, setViewingApp] = useState(null);
  const [deletingApp, setDeletingApp] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 4000);
  };

  const loadStats = useCallback(async () => {
    try {
      const response = await applicationService.getStats();
      if (response && response.success) {
        setStats(response.data);
      }
    } catch (err) {
      console.error('Failed to load stats:', err);
    }
  }, []);

  const loadApplications = useCallback(async () => {
    setLoading(true);
    try {
      const params = {
        search: search.trim() || undefined,
        status: status || undefined,
        workMode: workMode || undefined,
        sortBy,
        direction,
        page,
        size: 10
      };

      const response = await applicationService.getApplications(params);
      if (response && response.success) {
        const pageData = response.data;
        setApplications(pageData.content || []);
        const totalPgs = pageData.page?.totalPages ?? pageData.totalPages ?? (pageData.content?.length ? 1 : 0);
        const totalElems = pageData.page?.totalElements ?? pageData.totalElements ?? (pageData.content?.length || 0);
        setTotalPages(totalPgs);
        setTotalElements(totalElems);
        setIsApiConnected(true);
      }
    } catch (err) {
      console.error('Failed to load applications:', err);
      setIsApiConnected(false);
      showToast(err.message || 'Could not connect to backend service', 'error');
    } finally {
      setLoading(false);
    }
  }, [search, status, workMode, sortBy, direction, page]);

  // Initial load
  useEffect(() => {
    loadApplications();
    loadStats();
  }, [loadApplications, loadStats]);

  // Health check polling
  useEffect(() => {
    const checkConnection = async () => {
      try {
        await applicationService.checkHealth();
        setIsApiConnected(true);
      } catch {
        setIsApiConnected(false);
      }
    };
    checkConnection();
    const interval = setInterval(checkConnection, 15000);
    return () => clearInterval(interval);
  }, []);

  const handleResetFilters = () => {
    setSearch('');
    setStatus('');
    setWorkMode('');
    setSortBy('applicationDate');
    setDirection('desc');
    setPage(0);
  };

  const handleOpenCreateModal = () => {
    setEditingApp(null);
    setIsFormOpen(true);
  };

  const handleOpenEditModal = (app) => {
    setEditingApp(app);
    setIsFormOpen(true);
  };

  const handleFormSubmit = async (formData) => {
    setActionLoading(true);
    try {
      if (editingApp) {
        const response = await applicationService.updateApplication(editingApp.id, formData);
        showToast(response.message || 'Application updated successfully');
      } else {
        const response = await applicationService.createApplication(formData);
        showToast(response.message || 'Application created successfully');
      }
      setIsFormOpen(false);
      setEditingApp(null);
      loadApplications();
      loadStats();
    } catch (err) {
      showToast(err.message || 'Failed to save application', 'error');
    } finally {
      setActionLoading(false);
    }
  };

  const handleDeleteConfirm = async () => {
    if (!deletingApp) return;
    setActionLoading(true);
    try {
      await applicationService.deleteApplication(deletingApp.id);
      showToast(`Deleted application for ${deletingApp.companyName}`);
      setDeletingApp(null);
      loadApplications();
      loadStats();
    } catch (err) {
      showToast(err.message || 'Failed to delete application', 'error');
    } finally {
      setActionLoading(false);
    }
  };

  const handleQuickStatusChange = async (id, newStatus) => {
    try {
      const app = applications.find((a) => a.id === id);
      if (!app) return;
      const updatedPayload = { ...app, status: newStatus };
      await applicationService.updateApplication(id, updatedPayload);
      showToast(`Status updated to ${newStatus}`);
      if (viewingApp && viewingApp.id === id) {
        setViewingApp({ ...viewingApp, status: newStatus });
      }
      loadApplications();
      loadStats();
    } catch (err) {
      showToast(err.message || 'Failed to update status', 'error');
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      {/* Toast Notification */}
      {toast && (
        <div className="fixed bottom-6 right-6 z-50 animate-in slide-in-from-bottom-5">
          <div
            className={`flex items-center space-x-2.5 px-4 py-3 rounded-xl shadow-lg border text-sm font-medium ${
              toast.type === 'error'
                ? 'bg-rose-50 border-rose-200 text-rose-800'
                : 'bg-emerald-50 border-emerald-200 text-emerald-800'
            }`}
          >
            {toast.type === 'error' ? (
              <AlertCircle className="h-5 w-5 text-rose-600 flex-shrink-0" />
            ) : (
              <CheckCircle2 className="h-5 w-5 text-emerald-600 flex-shrink-0" />
            )}
            <span>{toast.message}</span>
          </div>
        </div>
      )}

      {/* Header */}
      <Navbar
        onOpenNewModal={handleOpenCreateModal}
        onRefresh={() => {
          loadApplications();
          loadStats();
        }}
        isConnected={isApiConnected}
        loading={loading}
      />

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Metric Cards KPI Row */}
        <div className="grid grid-cols-2 lg:grid-cols-5 gap-4 mb-8">
          <StatCard
            title="Total Applied"
            count={stats?.totalApplications ?? 0}
            icon={Briefcase}
            color="slate"
            subtext="Lifetime applications"
          />
          <StatCard
            title="Active Pipeline"
            count={stats?.activeApplications ?? 0}
            icon={Send}
            color="indigo"
            subtext="Applied, Screening, Interviewing"
          />
          <StatCard
            title="Interviews"
            count={stats?.interviewingCount ?? 0}
            icon={Users}
            color="amber"
            subtext="In active rounds"
          />
          <StatCard
            title="Offers"
            count={stats?.offeredCount ?? 0}
            icon={Award}
            color="emerald"
            subtext="Offers received"
          />
          <StatCard
            title="Rejections"
            count={stats?.rejectedCount ?? 0}
            icon={XCircle}
            color="rose"
            subtext="Archived rejections"
          />
        </div>

        {/* Filter Controls */}
        <ApplicationFilters
          search={search}
          onSearchChange={(val) => {
            setSearch(val);
            setPage(0);
          }}
          status={status}
          onStatusChange={(val) => {
            setStatus(val);
            setPage(0);
          }}
          workMode={workMode}
          onWorkModeChange={(val) => {
            setWorkMode(val);
            setPage(0);
          }}
          sortBy={sortBy}
          onSortByChange={setSortBy}
          direction={direction}
          onDirectionChange={setDirection}
          onReset={handleResetFilters}
        />

        {/* Application List Table */}
        <ApplicationList
          applications={applications}
          loading={loading}
          onView={(app) => setViewingApp(app)}
          onEdit={handleOpenEditModal}
          onDelete={(app) => setDeletingApp(app)}
          page={page}
          totalPages={totalPages}
          totalElements={totalElements}
          onPageChange={setPage}
        />
      </main>

      {/* Modals */}
      <ApplicationFormModal
        isOpen={isFormOpen}
        onClose={() => {
          setIsFormOpen(false);
          setEditingApp(null);
        }}
        onSubmit={handleFormSubmit}
        initialData={editingApp}
        loading={actionLoading}
      />

      <ApplicationDetailModal
        isOpen={!!viewingApp}
        onClose={() => setViewingApp(null)}
        application={viewingApp}
        onEdit={handleOpenEditModal}
        onStatusChange={handleQuickStatusChange}
      />

      <ConfirmDialog
        isOpen={!!deletingApp}
        onClose={() => setDeletingApp(null)}
        onConfirm={handleDeleteConfirm}
        title="Delete Job Application"
        message={`Are you sure you want to delete the application for "${deletingApp?.companyName}" (${deletingApp?.jobTitle})? This action cannot be undone.`}
        confirmText={actionLoading ? 'Deleting...' : 'Delete'}
        isDanger={true}
      />
    </div>
  );
}
