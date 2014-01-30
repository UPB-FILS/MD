//
//  MainViewController.h
//  Keyring
//
//  Created by Marin Iuliana on 12/01/14.
//  Copyright (c) 2014 Marin Iuliana. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MainViewController :  UIViewController <UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UIBarButtonItem *sidebarButton;
@property (nonatomic, retain) UITableView *tableView;
@end

//@interface MainViewController : UITableViewController
//@end


/*
@interface MainViewController: UIViewController <UITableViewDelegate, IBOutlet UITableView *tableView;>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@end
*/