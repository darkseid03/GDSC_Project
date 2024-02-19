const scroll = new LocomotiveScroll({
    el: document.querySelector('#main1'),
    smooth: true
});


function videoconAnimation(){
    var videocon = document.querySelector("#video-container")
    var playbtn = document.querySelector("#play")
    videocon.addEventListener("mouseenter", function(){
        gsap.to(playbtn,{
           scale:1,
           opacity:1
        })
    
    })
    videocon.addEventListener("mouseleave",function(){
        gsap.to(playbtn,{
            scale:0,
            opacity:0
        })
        
    })
    videocon.addEventListener("mousemove",function(dets){
        gsap.to(playbtn,{
            left:dets.x-70 ,
            top:dets.y-80
    
        })
    }) 
}  
videoconAnimation()

function loadinganimation(){
    gsap.from("#page11 h1",{
        y:30,
        opacity:0
        
    
    })
}
loadinganimation()

document.addEventListener("mousemove",function(dets){
    gsap.to("#cursor",{
        left:dets.x-100,
        top:dets.y-100
    })
})


  document.querySelectorAll(".child").forEach(function(elem){
    elem.addEventListener("mouseenter",function(){
       gsap.to("#cursor",{
         transform: 'translate(-50%, -50%) scale(1)'
       });
    });
    elem.addEventListener("mouseleave",function(){
        gsap.to("#cursor",{
          transform: 'translate(-50%, -50%) scale(0)'
        });
     });
});

  