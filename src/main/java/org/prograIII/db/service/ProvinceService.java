package org.prograIII.db.service;

import org.prograIII.db.dao.ProvinceDao;
import org.prograIII.db.model.ProvinceModel;

public class ProvinceService {
    private final ProvinceDao provinceDao;

    public ProvinceService() {
        this.provinceDao = new ProvinceDao();
    }

    // Guardar una provincia
    public boolean saveProvince(ProvinceModel province) {
        return provinceDao.save(province);
    }
}
