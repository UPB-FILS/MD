//
//  NoteDetailViewController.h
//  Keyring
//
//  Created by Marin Iuliana on 12/01/14.
//  Copyright (c) 2014 Marin Iuliana. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NoteDetailViewController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *subjectTextField;
@property (weak, nonatomic) IBOutlet UITextField *typeTextField;
@property (weak, nonatomic) IBOutlet UITextField *contentTextField;

@property (strong) NSManagedObject *note;
- (IBAction)cancel:(id)sender;
- (IBAction)save:(id)sender;

@end
