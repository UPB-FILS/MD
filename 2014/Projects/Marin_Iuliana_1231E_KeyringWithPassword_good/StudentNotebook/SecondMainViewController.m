//
//  SecondMainViewController.m
//  Keyring
//
//  Created by Marin Iuliana on 15/01/14.
//  Copyright (c) 2014 Marin Iuliana. All rights reserved.
//

#import "SecondMainViewController.h"

@interface SecondMainViewController()

@end

@implementation SecondMainViewController

- (NSString *) getFilePath {
    NSArray *pathArray = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    return [[pathArray objectAtIndex:0] stringByAppendingPathComponent:@"saved.plist"];
}

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
    // Do any additional setup after loading the view from its nib.
    //    NSDictionary *myArray;
    
    NSString* path = [[NSBundle mainBundle] pathForResource:@"passes"
                                                     ofType:@"txt"];
    NSString* content = [NSString stringWithContentsOfFile:path
                                                  encoding:NSUTF8StringEncoding
                                                     error:NULL];
    NSLog(@"%@",path);
    NSLog(@"%@",content);
    
    NSString *newContent = [NSString stringWithFormat:@"%@%@", content, content];
    [newContent writeToFile:path atomically:YES encoding:NSUTF8StringEncoding error:NULL];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end