package ar.com.fdvs.dj.test.subreport;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
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
import ar.com.fdvs.dj.test.TestRepositoryProducts;
import ar.com.fdvs.dj.test.util.EchoExpression;
import ar.com.fdvs.dj.util.SortUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.util.Collection;
import java.util.List;

public class SubSubReportTest extends BaseDjReportTest {

    private static final String SUBREPORT_L1_FIELD = "stuff"; // statistics
    private static final String SUBREPORT_L2_FIELD = "stuff"; // dummy3

    @Override
    public DynamicReport buildReport() throws Exception {

        DynamicReportBuilder builder = new DynamicReportBuilder();

        builder.addField(SUBREPORT_L1_FIELD, List.class.getName());

        DynamicReport statisticReport = generateStatisticReport();

        Subreport statisticSubReport = new SubReportBuilder()
                .setDataSource(DJConstants.SUBREPORT_PARAM_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, SUBREPORT_L1_FIELD)
                .setDynamicReport(statisticReport, new ClassicLayoutManager())
                .build();

        AbstractColumn nameColumn = ColumnBuilder.getNew().setTitle("Top Tier").setColumnProperty("name", String.class.getName()).setWidth(100).build();

        GroupBuilder groupBuilder = new GroupBuilder();
        DJGroup group = groupBuilder.setCriteriaColumn((PropertyColumn) nameColumn)
                .setGroupLayout(GroupLayout.VALUE_FOR_EACH)
                .addFooterSubreport(statisticSubReport)
                .build();

        builder.addGroup(group);
        builder.addColumn(nameColumn);

        AbstractColumn customExpCol = ColumnBuilder.getNew().setTitle("Main Expression").setCustomExpression(
                new EchoExpression("Top Expression")
        ).setWidth(150).build();
        builder.addColumn(customExpCol);

        DynamicReport dynamicReport = builder.build();

        dynamicReport.setReportName("Top Report");
        return dynamicReport;
    }

    private DynamicReport generateStatisticReport() {

        DynamicReportBuilder builder = new DynamicReportBuilder();
        AbstractColumn nameColumn = ColumnBuilder.getNew().setTitle("Second Tier").setColumnProperty("name", String.class.getName()).setWidth(100).build();

        builder.addField(SUBREPORT_L2_FIELD, List.class.getName());

        DynamicReport dummyReport = generateDummyReport();

        Subreport dummySubReport = new SubReportBuilder()
                .setDataSource(DJConstants.SUBREPORT_PARAM_ORIGIN_FIELD, DJConstants.DATA_SOURCE_TYPE_COLLECTION, SUBREPORT_L2_FIELD)
                .setDynamicReport(dummyReport, new ClassicLayoutManager())
                .build();

        GroupBuilder groupBuilder = new GroupBuilder();
        DJGroup group = groupBuilder.setCriteriaColumn((PropertyColumn) nameColumn)
                .setGroupLayout(GroupLayout.VALUE_FOR_EACH)
                .addFooterSubreport(dummySubReport)
                .build();

        builder.addGroup(group);
        builder.addColumn(nameColumn);

        AbstractColumn customExpCol = ColumnBuilder.getNew().setTitle("Sub Expression").setCustomExpression(
                new EchoExpression("Sub-Expression")
        ).setWidth(150).build();
        builder.addColumn(customExpCol);

        DynamicReport dynamicReport = builder.build();

        dynamicReport.setReportName("Sub-Report");
        return dynamicReport;
    }

    private DynamicReport generateDummyReport() {

        DynamicReportBuilder builder = new DynamicReportBuilder();
        AbstractColumn nameColumn = ColumnBuilder.getNew().setTitle("Third Tier").setColumnProperty("name", String.class.getName()).setWidth(100).build();

        GroupBuilder groupBuilder = new GroupBuilder();
        DJGroup group = groupBuilder.setCriteriaColumn((PropertyColumn) nameColumn)
                .setGroupLayout(GroupLayout.VALUE_FOR_EACH)
                .build();

        builder.addGroup(group);
        builder.addColumn(nameColumn);

        AbstractColumn customExpCol = ColumnBuilder.getNew().setTitle("Sub-Sub Expression").setCustomExpression(
                new EchoExpression("Sub-Sub-Expression")
        ).setWidth(150).build();
        builder.addColumn(customExpCol); // Comment out this line to see this report work. Seemingly, DJ cannot handle a sub-sub report with a CustomExpression.

        DynamicReport dynamicReport = builder.build();

        dynamicReport.setReportName("Sub-Sub-Report");
        return dynamicReport;
    }


    protected JRDataSource getDataSource() {
        Collection dummyCollection = TestRepositoryProducts.getNestedEntitiesCollection(); // NOT PRODUCTS!
        dummyCollection = SortUtils.sortCollection(dummyCollection, dr.getColumns());

        JRDataSource ds = new JRBeanCollectionDataSource(dummyCollection);		//Create a JRDataSource, the Collection used
        //here contains dummy hardcoded objects...
        return ds;
    }

    public static void main(String[] args) throws Exception {
        SubSubReportTest test = new SubSubReportTest();
        test.testReport();
        JasperViewer.viewReport(test.jp);
    }
}
