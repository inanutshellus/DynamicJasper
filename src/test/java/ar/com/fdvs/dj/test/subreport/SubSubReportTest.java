package ar.com.fdvs.dj.test.subreport;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.CustomExpression;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.SubReportBuilder;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.Subreport;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import ar.com.fdvs.dj.test.BaseDjReportTest;
import net.sf.jasperreports.view.JasperViewer;

import java.util.List;
import java.util.Map;

public class SubSubReportTest extends BaseDjReportTest {

    @Override
    public DynamicReport buildReport() throws Exception {

        DynamicReportBuilder builder = new DynamicReportBuilder();

        builder.addField("statistics", List.class.getName());

        DynamicReport statisticReport = generateStatisticReport();

        Subreport statisticSubReport = new SubReportBuilder()
                .setDataSource(DJConstants.SUBREPORT_PARAM_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, "statistics")
                .setDynamicReport(statisticReport, new ClassicLayoutManager())
                .build();

        AbstractColumn stateColumn = ColumnBuilder.getNew().setTitle("State").setColumnProperty("state", String.class.getName()).setWidth(100).build();

        GroupBuilder groupBuilder = new GroupBuilder();
        DJGroup group = groupBuilder.setCriteriaColumn((PropertyColumn) stateColumn)
                .setGroupLayout(GroupLayout.VALUE_FOR_EACH)
                .addFooterSubreport(statisticSubReport)
                .build();

        builder.addGroup(group);
        builder.addColumn(stateColumn);

        AbstractColumn customExpCol = ColumnBuilder.getNew().setTitle("Main Expression").setCustomExpression(
                new CustomExpression() {
                    public Object evaluate(Map fields, Map variables, Map parameters) {
                        return "Main Report";
                    }

                    public String getClassName() {
                        return String.class.getName();
                    }
                }
        ).setWidth(150).build();
        builder.addColumn(customExpCol);

        return builder.build();
    }

    private DynamicReport generateStatisticReport() {

        DynamicReportBuilder builder = new DynamicReportBuilder();
        AbstractColumn nameColumn = ColumnBuilder.getNew().setTitle("Area").setColumnProperty("name", String.class.getName()).setWidth(100).build();
        AbstractColumn pctColumn = ColumnBuilder.getNew().setTitle("Pct").setColumnProperty("percentage", Float.class.getName()).setWidth(100).build();

        builder.addField("dummy3", List.class.getName());

        DynamicReport dummyReport = generateDummyReport();

        Subreport dummySubReport = new SubReportBuilder()
                .setDataSource(DJConstants.SUBREPORT_PARAM_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, "dummy3")
                .setDynamicReport(dummyReport, new ClassicLayoutManager())
                .build();

        GroupBuilder groupBuilder = new GroupBuilder();
        DJGroup group = groupBuilder.setCriteriaColumn((PropertyColumn) nameColumn)
                .setGroupLayout(GroupLayout.VALUE_FOR_EACH)
                        .addFooterSubreport(dummySubReport)
                .build();

        builder.addGroup(group);
        builder.addColumn(nameColumn);
        builder.addColumn(pctColumn);

        AbstractColumn customExpCol = ColumnBuilder.getNew().setTitle("Sub Expression").setCustomExpression(
                new CustomExpression() {
                    public Object evaluate(Map fields, Map variables, Map parameters) {
                        return "Sub-report";
                    }

                    public String getClassName() {
                        return String.class.getName();
                    }
                }
        ).setWidth(150).build();
        builder.addColumn(customExpCol);

        return builder.build();
    }

    private DynamicReport generateDummyReport() {

        DynamicReportBuilder builder = new DynamicReportBuilder();
        AbstractColumn nameColumn = ColumnBuilder.getNew().setTitle("Name").setColumnProperty("name", String.class.getName()).setWidth(100).build();
        AbstractColumn numberColumn = ColumnBuilder.getNew().setTitle("Num").setColumnProperty("number", Long.class.getName()).setWidth(100).build();

        GroupBuilder groupBuilder = new GroupBuilder();
        DJGroup group = groupBuilder.setCriteriaColumn((PropertyColumn) nameColumn)
                .setGroupLayout(GroupLayout.VALUE_FOR_EACH)
                .build();

        builder.addGroup(group);
        builder.addColumn(nameColumn);
        builder.addColumn(numberColumn);

        AbstractColumn customExpCol = ColumnBuilder.getNew().setTitle("Sub-Sub Expression").setCustomExpression(
                new CustomExpression() {
                    public Object evaluate(Map fields, Map variables, Map parameters) {
                        return "Sub-Sub-report";
                    }

                    public String getClassName() {
                        return String.class.getName();
                    }
                }
        ).setWidth(150).build();
        builder.addColumn(customExpCol); // Comment out this line to see this report work. Seemingly, DJ cannot handle a sub-sub report with a CustomExpression.

        return builder.build();
    }

    public static void main(String[] args) throws Exception {
        SubSubReportTest test = new SubSubReportTest();
        test.testReport();
        JasperViewer.viewReport(test.jp);
    }
}
