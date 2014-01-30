//
//  NoteDetailViewController.m
//  Keyring
//
//  Created by Marin Iuliana on 12/01/14.
//  Copyright (c) 2014 Marin Iuliana. All rights reserved.
//

#import "NoteDetailViewController.h"

@interface NoteDetailViewController ()

@end

@implementation NoteDetailViewController
@synthesize note;

- (NSManagedObjectContext *)managedObjectContext {
    NSManagedObjectContext *context = nil;
    id delegate = [[UIApplication sharedApplication] delegate];
    if ([delegate performSelector:@selector(managedObjectContext)]) {
        context = [delegate managedObjectContext];
    }
    return context;
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
	// Do any additional setup after loading the view.
    if (self.note) {
        [self.subjectTextField setText:[self.note valueForKey:@"subject"]];
        [self.typeTextField setText:[self.note valueForKey:@"type"]];
        [self.contentTextField setText:[self.note valueForKey:@"content"]];
    }

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)cancel:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)save:(id)sender {
    NSManagedObjectContext *context = [self managedObjectContext];
    
    if (self.note) {
        // Update existing note
        [self.note setValue:self.subjectTextField.text forKey:@"subject"];
        [self.note setValue:self.typeTextField.text forKey:@"type"];
        [self.note setValue:self.contentTextField.text forKey:@"content"];

    } else {
        // Create a new note
        NSManagedObject *newNote = [NSEntityDescription insertNewObjectForEntityForName:@"Note" inManagedObjectContext:context];
        [newNote setValue:self.subjectTextField.text forKey:@"subject"];
        [newNote setValue:self.typeTextField.text forKey:@"type"];
        [newNote setValue:self.contentTextField.text forKey:@"content"];
    }
    
    NSError *error = nil;
    // Save the object to persistent store
    if (![context save:&error]) {
        NSLog(@"Can't Save! %@ %@", error, [error localizedDescription]);
    }
    
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
