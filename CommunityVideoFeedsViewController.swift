//
//  CommunityContainerViewController.swift
//  MobileFirstEnt
//
//  Created by Anastacio Gianareas on 6/23/15.
//  Copyright (c) 2015 mfe. All rights reserved.
//

import UIKit

class CommunityVideoFeedsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

	@IBOutlet weak var videoFeedTableView: UITableView!
	@IBOutlet weak var menuButtonItem: UIBarButtonItem!
	var userInfo = (UIApplication.sharedApplication().delegate as! AppDelegate).userInfo
	
	var newsFeedVideos: [Video] = [Video]()
	var helpers:Helpers = Helpers()
	var config = Config()
	
	
	var cacheImage = NSCache()
	
	
    override func viewDidLoad() {
        super.viewDidLoad()
		
		self.helpers.tablePullForRefresh(self, table: self.videoFeedTableView, callbackActionAsSelector: "loadNewsFeed", message: "")
		self.videoFeedTableView.hidden = true
		
		var imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 80, height: 40))
		imageView.contentMode = .ScaleAspectFit
		var image = UIImage(named: "mfe_logo_2_white.png")
		imageView.image = image
		self.navigationItem.titleView = imageView
		
		self.setupUser()
		
    }
	override func viewWillAppear(animated: Bool) {
		//self.videoFeedTableView.hidden = true
		//self.setupUser()
	}
	@IBAction func refreshTableViewController(sender: AnyObject) {
		self.helpers.showLoadingIcon(self.view, target: self)
		self.loadNewsFeed()
	}
	
	func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return newsFeedVideos.count
	}
	
	
	func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
		var i = indexPath.row
		
		var cell = videoFeedTableView.dequeueReusableCellWithIdentifier("CommunityVideoFeedsCell") as! CommunityVideoFeedsTableViewCell
		
		
		cell.titleLabel.text = newsFeedVideos[i].title.uppercaseString
		
		cell.thumnbailImage.image = UIImage(named: "mfe_logo_2_black.png")
		cell.appLogoImage.image = UIImage(named: "mfe_logo_2_black.png")
		
		
		//Using cached and proper way of downloadin images
		if newsFeedVideos[i].thumbnails.count > 0 {
			

			if let img = cacheImage.objectForKey(newsFeedVideos[i].thumbnails[0].url)  as? UIImage {
				cell.thumnbailImage.image = img
				println("Cached thumbnail \(i)")
			} else {
				var url = NSURL(string: newsFeedVideos[i].thumbnails[0].url)
				
				let dataTask = NSURLSession.sharedSession().dataTaskWithURL(url!, completionHandler: { (data:NSData!, response:NSURLResponse!, error: NSError!) -> Void in
					
					
					if let image = UIImage(data: data){
						
						//background thread
						
						
						//Decomrpess image (Backgroudn thread)
						UIGraphicsBeginImageContext(image.size)
						image.drawAtPoint(CGPointZero)
						UIGraphicsEndImageContext()
						var newImage = self.config.resizeImage(image, x: 0.8, y: 0.8)
						
						//main thread
						dispatch_async(dispatch_get_main_queue(), { () -> Void in
							//Do this in main thread (Dont compress in main thread)
							//self.imageCache.setObject(image, forKey: appData.URL)
								println("Downloaded thumbnail \(i)")
							self.cacheImage.setObject(newImage, forKey: self.newsFeedVideos[i].thumbnails[0].url)
							
				
							cell.thumnbailImage.image = newImage
							cell.setNeedsLayout() //Redraw
							
						})
						
						
					}
					
					
				})
				
				dataTask.resume()
			}
			
		}
		
		if let img = cacheImage.objectForKey(newsFeedVideos[i].applicationLogoUrl)  as? UIImage {
			cell.appLogoImage.image = img
			println("Cached Logo \(i)")
		} else {
			var url = NSURL(string: newsFeedVideos[i].applicationLogoUrl)
			
			let dataTask = NSURLSession.sharedSession().dataTaskWithURL(url!, completionHandler: { (data:NSData!, response:NSURLResponse!, error: NSError!) -> Void in
				
				
				if let image = UIImage(data: data){
					
					//background thread
					
					
					//Decomrpess image (Backgroudn thread)
					UIGraphicsBeginImageContext(image.size)
					image.drawAtPoint(CGPointZero)
					UIGraphicsEndImageContext()
					
					var newImage = self.config.resizeImage(image, x: 0.2, y: 0.2)
					//main thread
					dispatch_async(dispatch_get_main_queue(), { () -> Void in
						//Do this in main thread (Dont compress in main thread)
						//self.imageCache.setObject(image, forKey: appData.URL)
						println("Downloaded Logo \(i)")
						self.cacheImage.setObject(newImage, forKey: self.newsFeedVideos[i].applicationLogoUrl)
						
					
						cell.appLogoImage.image = newImage
						cell.setNeedsLayout() //Redraw
						
					})
					
					
				}
				
				
			})
			
			dataTask.resume()
		}
		
		
		
		//cell.appLogoImage.image = newsFeedVideos[i].applicationImage
		cell.appTitleLabel.text = newsFeedVideos[i].applicationTitle.uppercaseString
		
		cell.thumnbailImage.layer.masksToBounds = true
		cell.thumnbailImage.layer.borderColor = UIColor.blackColor().CGColor
		cell.thumnbailImage.layer.borderWidth = 1
		cell.thumnbailImage.clipsToBounds = true
		
		//Gesture clicking
		cell.tapGestureCell = UITapGestureRecognizer(target: self, action: Selector("showVideoCell:"))
		cell.tapGestureCell.numberOfTapsRequired = 1
		cell.cellView.tag = i //This holds current array index in the view
		cell.cellView.addGestureRecognizer(cell.tapGestureCell)
		cell.cellView.userInteractionEnabled = true
		
		
		cell.appLogoImage.layer.cornerRadius = 30
		cell.appLogoImage.layer.masksToBounds = true
		cell.appLogoImage.layer.backgroundColor = UIColor.blackColor().CGColor
		cell.appLogoImage.layer.borderColor = UIColor.grayColor().CGColor
		cell.appLogoImage.layer.borderWidth = 1
		cell.appLogoImage.clipsToBounds = true
		

		
		return cell
	}
	
	
	func setupUser(){
		ServiceCall.getUserId { (guidId) -> () in
			self.userInfo.userId = guidId!
			(UIApplication.sharedApplication().delegate as! AppDelegate).userInfo = self.userInfo
			self.loadNewsFeed()
			self.getUserApps()
		}
	}
	func getUserApps(){
		ServiceCall.getUserListOfApps({ () -> () in
			self.menuButtonItem.enabled = true
		})
	}
	func loadNewsFeed(){
		self.helpers.showLoadingIcon(self.view, target: self)
		
		ServiceCall.getUserNewsFeed { (videoList) -> () in
			self.helpers.tablePullForRefreshHide(self.videoFeedTableView)

			self.newsFeedVideos = videoList
			self.videoFeedTableView.reloadData()
			self.helpers.hideLoadingIcon(self.view, target: self)
			self.videoFeedTableView.hidden = false
		}
	}
	
	
	
	func showVideoCell (sender: UIGestureRecognizer){
		var view: UIView = sender.view!
		var i = view.tag
		
		var videoPlayerViewController: VideoPlayerViewController
		videoPlayerViewController = self.storyboard?.instantiateViewControllerWithIdentifier("videoPlayerStoryboard") as! VideoPlayerViewController
		videoPlayerViewController.video = newsFeedVideos[i]
		
		
		self.navigationController?.showViewController(videoPlayerViewController, sender: self)
		//self.navigationController?.pushViewController(videoPlayerViewController, animated: true)
		//self.navigationController?.presentViewController(videoPlayerViewController, animated: true, completion: nil)
		
		
	}

	

}
