package com.example.psoftprojectg7.Dashboard.model;

public class RevenueObj {
    private String plan;
    private Metric metric;

    public RevenueObj(String plan, Metric metric) {
        this.plan = plan;
        this.metric = metric;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public static class Metric {
        private int activeSubscriptions;
        private int newSubscriptions;
        private int cancelledSubscriptions;
        private double churnRate;
        private double revenue;

        public Metric(int activeSubscriptions, int newSubscriptions, int cancelledSubscriptions, double churnRate, double revenue) {
            this.activeSubscriptions = activeSubscriptions;
            this.newSubscriptions = newSubscriptions;
            this.cancelledSubscriptions = cancelledSubscriptions;
            this.churnRate = churnRate;
            this.revenue = revenue;
        }

        public int getActiveSubscriptions() {
            return activeSubscriptions;
        }

        public void setActiveSubscriptions(int activeSubscriptions) {
            this.activeSubscriptions = activeSubscriptions;
        }

        public int getNewSubscriptions() {
            return newSubscriptions;
        }

        public void setNewSubscriptions(int newSubscriptions) {
            this.newSubscriptions = newSubscriptions;
        }

        public int getCancelledSubscriptions() {
            return cancelledSubscriptions;
        }

        public void setCancelledSubscriptions(int cancelledSubscriptions) {
            this.cancelledSubscriptions = cancelledSubscriptions;
        }

        public double getChurnRate() {
            return churnRate;
        }

        public void setChurnRate(double churnRate) {
            this.churnRate = churnRate;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }
    }
}
