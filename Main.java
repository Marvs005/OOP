
        public class Main {

            public static void main(String[] args) {
        }
            private String workMonth;
            private int workHours;
            private int workDays;
            private int lates;
            private int absences;

            public Main(String workMonth, int workHours, int workDays, int lates, int absences) {
                this.workMonth = workMonth;
                this.workHours = workHours;
                this.workDays = workDays;
                this.lates = lates;
                this.absences = absences;
            }

            // Getters and Setters
            public String getWorkMonth() {
                return workMonth;
            }

            public void setWorkMonth(String workMonth) {
                this.workMonth = workMonth;
            }

            public int getWorkHours() {
                return workHours;
            }

            public void setWorkHours(int workHours) {
                this.workHours = workHours;
            }

            public int getWorkDays() {
                return workDays;
            }

            public void setWorkDays(int workDays) {
                this.workDays = workDays;
            }

            public int getLates() {
                return lates;
            }

            public void setLates(int lates) {
                this.lates = lates;
            }

            public int getAbsences() {
                return absences;
            }

            public void setAbsences(int absences) {
                this.absences = absences;

        }
    }