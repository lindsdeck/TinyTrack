package com.childcare.enrollment.model;

public class DashboardSummary {

    private final long activeStudentCount;
    private final long activeClassroomCount;
    private final int totalLicensedCapacity;
    private final long currentOpenings;

    public DashboardSummary(
            long activeStudentCount,
            long activeClassroomCount,
            int totalLicensedCapacity,
            long currentOpenings) {

        this.activeStudentCount = activeStudentCount;
        this.activeClassroomCount = activeClassroomCount;
        this.totalLicensedCapacity = totalLicensedCapacity;
        this.currentOpenings = currentOpenings;
    }

    public long getActiveStudentCount() {
        return activeStudentCount;
    }

    public long getActiveClassroomCount() {
        return activeClassroomCount;
    }

    public int getTotalLicensedCapacity() {
        return totalLicensedCapacity;
    }

    public long getCurrentOpenings() {
        return currentOpenings;
    }
}
