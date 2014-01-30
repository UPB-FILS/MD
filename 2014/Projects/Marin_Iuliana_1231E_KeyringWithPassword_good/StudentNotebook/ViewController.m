//
//  ViewController.m
//  Keyring
//
//  Created by Marin Iuliana on 15/01/14.
//  Copyright (c) 2014 Marin Iuliana. All rights reserved.
//

#import "ViewController.h"
#import "NoteViewController.h"

@interface ViewController ()
@property (weak, nonatomic) IBOutlet UITextField *PassField;
- (IBAction)submitButton:(id)sender;

@end

@implementation ViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)submitButton:(id)sender {
   
    if([_PassField.text isEqualToString:@"Iuliana"])
    {
       
        [self performSegueWithIdentifier:@"NextView" sender:self];
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"SORRY!"
                                                        message:@"Wrong typed password!"
                                                       delegate:nil
                                              cancelButtonTitle:@"Try Again!"
                                              otherButtonTitles:nil];
        [alert show];
    }
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    
    return [textField resignFirstResponder];
}



@end
