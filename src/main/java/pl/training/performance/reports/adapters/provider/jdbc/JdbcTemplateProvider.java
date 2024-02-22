package pl.training.performance.reports.adapters.provider.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.domain.PageSpec;
import pl.training.performance.reports.domain.ResultPage;
import pl.training.performance.reports.ports.DataProvider;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcTemplateProvider implements DataProvider {

    private static final String INSERT_ENTRY = "insert into entries (id, order_id, region, country, item_type, is_online_sale_channel, order_priority, order_date, ship_date, units_sold, unit_price, unit_cost, total_revenue, total_cost, total_profit) values (:id, :orderId, :region, :country, :itemType, :isOnlineSaleChannel, :orderPriority, :orderDate, :shipDate, :unitsSold, :unitPrice, :unitCost, :totalRevenue, :totalCost, :totalProfit)";
    private static final String SELECT_ALL = "select * from entries offset :offset limit :limit";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final EntryRowMapper rowMapper = new EntryRowMapper();
    private DataChangeDelegate dataChangeDelegate = () -> {};

    public JdbcTemplateProvider(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static class EntryRowMapper implements RowMapper<DataEntry> {


        @Override
        public DataEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }

    }

    @Override
    public ResultPage<DataEntry> findAll(PageSpec pageSpec) {
        var parameters = new MapSqlParameterSource()
                .addValue("offset", pageSpec.getOffset())
                .addValue("limit", pageSpec.pageSize());
        var rows = jdbcTemplate.query(SELECT_ALL, parameters, rowMapper);
        int totalPages = (int) Math.ceil((double) 5_000_000 / pageSpec.pageSize());
        return new ResultPage<>(rows, totalPages);
    }

    @Override
    public void add(DataEntry dataEntry) {
        jdbcTemplate.update(INSERT_ENTRY, new BeanPropertySqlParameterSource(dataEntry));
        dataChangeDelegate.dataChanged();
    }

    @Override
    public void setDelegate(DataChangeDelegate dataChangeDelegate) {
        this.dataChangeDelegate = dataChangeDelegate;
    }

}
