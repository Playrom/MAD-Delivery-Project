package it.polito.justorder_framework.abstract_activities;

public class AbstractListViewWithSidenavSave extends ActivityAbstractWithSideNavSave {
    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.initDataSource();
    }

    protected void initDataSource() {}
}