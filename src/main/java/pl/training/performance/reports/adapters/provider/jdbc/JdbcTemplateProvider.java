package pl.training.performance.reports.adapters.provider.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.domain.OrderPriority;
import pl.training.performance.reports.domain.PageSpec;
import pl.training.performance.reports.domain.ResultPage;
import pl.training.performance.reports.ports.DataProvider;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class JdbcTemplateProvider implements DataProvider {

    private static final String INSERT_ENTRY = "insert into entries (id, order_id, region, country, item_type, is_online_sale_channel, order_priority, order_date, ship_date, units_sold, unit_price, unit_cost, total_revenue, total_cost, total_profit) values (:id, :orderId, :region, :country, :itemType, :isOnlineSaleChannel, :orderPriority, :orderDate, :shipDate, :unitsSold, :unitPrice, :unitCost, :totalRevenue, :totalCost, :totalProfit)";

    private static final String SELECT_ALL = "select * from entries e offset :offset limit :limit";
    private static final String SELECT_COUNT_ALL = "select count(e.id) from entries e";

    // select item_type, sum(total_profit) from entries where order_date > to_date('2012-01-01', 'yyyy-MM-dd') and order_date < to_date('2013-01-01', 'yyyy-MM-dd') group by item_type;
    /*
    create index item_type on entries (item_type);
    create index item_type on entries (order_date);
     */
    /*private static final String SELECT_COUNT_ALL = "select count(e.id) from entries e where e.order_date > :after and e.order_date < :before";
    private static final String SELECT_ALL = "select * from entries e where e.order_date > :after and e.order_date < :before offset :offset limit :limit";*/

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final EntryRowMapper rowMapper = new EntryRowMapper();
    private DataChangeDelegate dataChangeDelegate = () -> {};

    public JdbcTemplateProvider(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static class EntryRowMapper implements RowMapper<DataEntry> {

        @Override
        public DataEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DataEntry(
                    rs.getString("region"),
                    rs.getString("country"),
                    rs.getString("item_type"),
                    rs.getBoolean("is_online_sale_channel"),
                    OrderPriority.values()[rs.getInt("order_priority")],
                    rs.getDate("order_date").toLocalDate(),
                    rs.getLong("order_id"),
                    rs.getDate("ship_date").toLocalDate(),
                    rs.getInt("units_sold"),
                    rs.getBigDecimal("unit_price"),
                    rs.getBigDecimal("unit_cost"),
                    rs.getBigDecimal("total_revenue"),
                    rs.getBigDecimal("total_cost"),
                    rs.getBigDecimal("total_profit")
            );
        }

    }

    @Override
    public ResultPage<DataEntry> findAll(PageSpec pageSpec) {
        var parameters = new MapSqlParameterSource()
                .addValue("offset", pageSpec.getOffset())
                .addValue("limit", pageSpec.pageSize());
                /*.addValue("after", LocalDate.of(2012, 1, 1))
                .addValue("before", LocalDate.of(2013, 1, 1));*/
        var rows = jdbcTemplate.query(SELECT_ALL, parameters, rowMapper);
        int totalPages = (int) Math.ceil((double) jdbcTemplate.queryForObject(SELECT_COUNT_ALL, parameters, Integer.class)
                / pageSpec.pageSize());
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
