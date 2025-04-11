package org.prograIII.db.service;

import org.prograIII.db.dao.ReportDao;
import org.prograIII.db.model.ReportModel;

public class ReportService {
    private final ReportDao reportDao;

    public ReportService() {
        this.reportDao = new ReportDao();
    }

    // Guardar un reporte
    public boolean saveReport(ReportModel report) {
        return reportDao.save(report);
    }
}
