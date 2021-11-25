var app = angular.module('offsetapp',
						 ["offsetapp.controllers", "offsetapp.directives",  "ngRoute"],
                                                 function($routeProvider) {
                                                         $routeProvider
														 .when("/", {
															 templateUrl: "views/grouplist.html",
															 controller: "GroupListCtrl"
														 })
                                                         .when("/group/:group", {
                                                             templateUrl: "views/group.html",
                                                             controller: "GroupCtrl"
                                                         })
                                                         .when("/group/:group/:topic", {
                                                             templateUrl: "views/topic.html",
                                                             controller: "TopicCtrl"
                                                         })
                                                         .when("/clusterviz", {
                                                              templateUrl: "views/cluster-viz.html",
                                                              controller: "ClusterVizCtrl"
                                                         })
                                                         .when("/activetopicsviz", {
                                                               templateUrl: "views/activetopics-viz.html",
                                                               controller: "ActiveTopicsVizCtrl"
                                                          })
                                                         .when("/topics", {
                                                              templateUrl: "views/topiclist.html",
                                                              controller: "TopicListCtrl"
                                                          })
                                                         .when("/topicdetail/:topic", {
                                                              templateUrl: "views/topic-detail.html",
                                                              controller: "TopicDetailCtrl"
                                                          })
                                                         .when("/topic/:topic/consumers", {
                                                              templateUrl: "views/topic-consumers.html",
                                                              controller: "TopicConsumersCtrl"
                                                         });;
                                                 });

angular.module("offsetapp.services", ["ngResource"])
	.factory("offsetinfo", ["$resource", "$http", function($resource, $http) {
		function processConsumer(cb) {
			return function(data) {
				data.offsets = groupPartitions(data.offsets);
				cb(data);
			}
		}

		function processMultipleConsumers(cb) {
			return function(data) {
				_(data.consumers.active).forEach(function(consumer) {consumer.offsets = groupPartitions(consumer.offsets);});
				_(data.consumers.inactive).forEach(function(consumer) {consumer.offsets = groupPartitions(consumer.offsets);});
				cb(data);
			};
		}

		function groupPartitions(data) {
			var groups = _(data).groupBy(function(p) {
				var t = p.timestamp;
				if(!t) t = 0;
				return p.group+p.topic+t.toString();
			});
			groups = groups.values().map(function(partitions) {
				return {
					group: partitions[0].group,
					topic: partitions[0].topic,
					partitions: partitions,
					logSize: _(partitions).pluck("logSize").reduce(function(sum, num) {
						return sum + num;
					}),
					offset: _(partitions).pluck("offset").reduce(function(sum, num) {
						return sum + num;
					}),
					timestamp: partitions[0].timestamp
				};
			}).value();
			return groups;
		}

		return {
			getGroup: function(group, cb) {
				return $resource("./group/:group").get({group:group}, processConsumer(cb));
			},
			topicDetail: function(topic, cb) {
            	return $resource("./topicdetails/:topic").get({topic:topic}, cb);
            },
			topicConsumers: function(topic, cb) {
            	return $resource("./topic/:topic/consumer").get({topic:topic}, processMultipleConsumers(cb));
            },
            loadClusterViz: function(group, cb) {
                cb(loadViz("#dataviz-container", "./clusterlist"))
            },
            loadTopicConsumerViz: function(group, cb) {
                cb(loadViz("#dataviz-container", "./activetopics"))
            },
			listGroup: function() {return $http.get("./group");},
			listTopics: function() {return $http.get("./topiclist");},
			getTopic: function(group, topic, cb) {
				return $resource("./group/:group/:topic").get({group:group, topic: topic}, processConsumer(cb));
			}
		};
	}]);
