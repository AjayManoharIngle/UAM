package org.rebit.auth.config;

class EnableDataSourceNew{
		
		private String datasourceName;
		private boolean isJndiDataSourceUsed;
		
		public String getDatasourceName() {
			return datasourceName;
		}



		public void setDatasourceName(String datasourceName) {
			this.datasourceName = datasourceName;
		}



		public boolean isJndiDataSourceUsed() {
			return isJndiDataSourceUsed;
		}



		public void setJndiDataSourceUsed(boolean isJndiDataSourceUsed) {
			this.isJndiDataSourceUsed = isJndiDataSourceUsed;
		}



		@Override
		public String toString() {
			return "EnableDataSourceNew [datasourceName=" + datasourceName + ", isJndiDataSourceUsed="
					+ isJndiDataSourceUsed + "]";
		}
	}