package com.mr.data.modules.api.xls.export.domain.common;

import com.mr.data.modules.api.xls.domain.BaseModel;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/6.
 */
public class ExportConfig extends BaseModel {
    private String fileName;//输出的文件名
    private ExportType exportType = ExportType.EXCEL2007;//0 表示 excel, 1 表示txt
    private List<ExportCell> exportCells;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ExportCell> getExportCells() {
        return exportCells;
    }

    public void setExportCells(List<ExportCell> exportCells) {
        this.exportCells = exportCells;
    }

    public ExportType getExportType() {
        return exportType;
    }

    public void setExportType(ExportType exportType) {
        this.exportType = exportType;
    }
}
