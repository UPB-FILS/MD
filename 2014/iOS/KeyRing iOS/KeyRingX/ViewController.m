//
//  ViewController.m
//  KeyRingX
//
//  Created by Tudor on 1/6/14.
//  Copyright (c) 2014 Student. All rights reserved.
//

#import "ViewController.h"
#import "SecondViewController.h"

@interface ViewController ()
@property (weak, nonatomic) IBOutlet UITextField *masterPassField;
- (IBAction)okButton:(id)sender;
//@property (weak, nonatomic) IBOutlet UILabel *showPass;

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    self.masterPassField.delegate = self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)okButton:(id)sender {
    SecondViewController *second=[[SecondViewController alloc] initWithNibName:nil bundle:nil];
    
    if([_masterPassField.text isEqualToString:@"ceva"])
    {
//        _showPass.text = _masterPassField.text;
        [self presentViewController:second animated:YES completion:NULL];
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Oups!"
                                                        message:@"Wrong master password!"
                                                       delegate:nil
                                              cancelButtonTitle:@"Try Again"
                                              otherButtonTitles:nil];
        [alert show];
    }
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
//    _showPass.text = _masterPassField.text;
    return [textField resignFirstResponder];
}
@end
