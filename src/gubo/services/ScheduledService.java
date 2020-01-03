package gubo.services;

import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ScheduledService implements BaseService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private DataSource datasource = null;
	@Override
	public DataSource getDatasource() {
		// TODO Auto-generated method stub
		return datasource;
	}

	@Override
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	

	volatile boolean stop = false;
	public String getName() {
		return this.getClass().getSimpleName();
	};
	private Timer timer = new Timer(this.getName());
	
	private TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			if (!stop) {
				try {
					oneRound();
				} catch (InterruptedException e) {
					logger.error("Interrupted sleep", e);
				} catch (Exception e) {
					logger.error("Unexpected Exception in oneRound.", e);
				}
			}
		}

	};
	// 子类要在这个 方法里做该干的事。
	abstract public void oneRound() throws Exception;
	
	abstract public long getDelay();
	abstract public long getPeriod();
	
	public void start() {
		timer.schedule(timerTask, this.getDelay(), this.getPeriod());
	}

	public void stop() {
		this.stop = true;
		this.timer.cancel();
	}

}
