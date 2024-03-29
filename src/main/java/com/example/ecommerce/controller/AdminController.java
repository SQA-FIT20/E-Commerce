package com.example.ecommerce.controller;

import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.request.UpdateAdminRequest;
import com.example.ecommerce.dto.request.auth.ChangeAccessRequest;
import com.example.ecommerce.dto.request.deliveryPartner.CreateDeliveryPartnerRequest;
import com.example.ecommerce.dto.request.product.UpdateProductRequest;
import com.example.ecommerce.dto.request.promotion.CreatePromotionRequest;
import com.example.ecommerce.dto.request.promotion.UpdatePromotionRequest;
import com.example.ecommerce.dto.response.Response;
import com.example.ecommerce.service.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
public class AdminController {


    @Value("${default.elementPerPage}")
    private String defaultElementPerPage;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    @Operation(
            summary = "Get all users",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all account successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Get all account successfully!",
                                                "data": [
                                                    {
                                                        "id": 1 ,
                                                        "email": "quangnam@gmail.com",
                                                        "avatar" : "https:jadjkh.com",
                                                        "role" : "CUSTOMER",
                                                        "isLooked" : false
                                                    },
                                                    {
                                                        "id": 2 ,
                                                        "email": "quangnam@gmail.com",
                                                        "avatar" : "https:jadjkh.com",
                                                        "role" : "STORE",
                                                        "isLooked" : false
                                                    },
                                                    {
                                                        "id": 3 ,
                                                        "email": "quangnam@gmail.com",
                                                        "avatar" : "https:jadjkh.com",
                                                        "role" : "DELIVERY_PARTNER",
                                                        "isLooked" : false
                                                    }
                                                ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Get all account failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Get all account failed!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @GetMapping("/manage-accounts")
    public ResponseEntity<Response> getAllUsers(@RequestParam(defaultValue = "0", required = false) Integer page,
                                                @RequestParam(defaultValue = "0",  required = false) Integer elementsPerPage,
                                                @RequestParam(defaultValue = "createdAt",  required = false) String filter,
                                                @RequestParam(defaultValue = "desc",  required = false) String sortBy,
                                                @RequestParam(defaultValue = "all", required = false) String status,
                                                @RequestParam(defaultValue = "all", required = false) String role) {
        if (elementsPerPage == 0) {
            elementsPerPage = Integer.parseInt(defaultElementPerPage);
        }
        return userService.getAllUsers(page, elementsPerPage, filter, sortBy, status, role);
    }

    @Operation(
            summary = "Get user by id",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get account by id successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Get account by id successfully!",
                                                "data": {
                                                        "id": 1 ,
                                                        "email": "quangnam@gmail.com",
                                                        "avatar" : "https:jadjkh.com",
                                                        "role" : "CUSTOMER",
                                                        "isLooked" : false
                                                    }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Get account by id failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Not found account!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @GetMapping("/manage-accounts/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable @Schema(description = "id of user") Long id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Change access of user",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangeAccessRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "operation": "LOCK"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update account successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Update account successfully!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Update account failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Update account failed!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @PutMapping("/manage-accounts")
    public ResponseEntity<Response> changeAccess(@RequestBody ChangeAccessRequest request) {
        return userService.changeAccess(request);
    }


    @Operation(
            summary = "Update product",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateProductRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "productId": 1,
                                        "name": "Iphone 12",
                                        "price": 10000000,
                                        "description": "Iphone 12",
                                        "category": "Technology",
                                        "images": "https://cdn.tgdd.vn/Products/Images/42/220533/iphone-12-pro-max-green-600x600.jpg"
                                    }
                                    """)
                    )

            )


    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update product successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Update product successfully!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Update product failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Update product failed!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @PutMapping("/manage-products")
    public ResponseEntity<Response> updateProduct(@RequestBody UpdateProductRequest request) {
        return productService.updateProduct(request);
    }

    @Operation(
            summary = "Delete product by id",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Delete product successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Delete product successfully!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found the account",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Not found the account!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Delete product failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Delete product failed!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Response> deleteProductById(@PathVariable @Schema(description = "id of product") Long id) {
        return productService.deleteProductById(id);
    }

    @Operation(
            summary = "Get all feedbacks",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get feedback successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Get feedback for product successfully!",
                                                "data": [{
                                                    "id" = 1,
                                                    "comment": "The app is good",
                                                    "isRead" : true,
                                                    "UserId" : 12
                                                },
                                                {
                                                    "id" = 2,
                                                    "comment": "The app is bad",
                                                    "isRead" : false,
                                                    "UserId" : 35
                                                },
                                                {
                                                    "id" = 3,
                                                    "comment": "The app is ok",
                                                    "isRead" : false,
                                                    "UserId" : 100
                                                }
                                                ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Get feedback failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Get feedback for product failed!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @GetMapping("/feedbacks")
    public ResponseEntity<Response> getFeedbacks(@RequestParam(defaultValue = "0", required = false) Integer page,
                                                 @RequestParam(defaultValue = "0", required = false) Integer elementsPerPage,
                                                 @RequestParam(defaultValue = "all", required = false) String status) {
        if (elementsPerPage == 0) {
            elementsPerPage = Integer.parseInt(defaultElementPerPage);
        }
        return feedbackService.getFeedbacks(page, elementsPerPage, status);
    }

    @Operation(
            summary = "Get feedback by id",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get feedback successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Get feedback by id successfully!",
                                                "data": {
                                                    "id" : 1,
                                                    "comment" : "the app is good",
                                                    "isRead" : false,
                                                    "userId": 12
                                                }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Get feedback failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Wrong id of feedback!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found the feedback!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Not found the feedback!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @GetMapping("/feedbacks/{id}")
    public ResponseEntity<Response> getFeedbacksById(@PathVariable @Schema(description = "id of feedback") Long id) {
        return feedbackService.findById(id);
    }

    @Operation(
            summary = "Mark feedback as read by id",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Mark feedback as read successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Mark feedback as read successfully!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found the feedback id!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Wrong id of feedback!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @PutMapping("/feedbacks/{id}")
    public ResponseEntity<Response> markFeedbackAsRead(@PathVariable @Schema(description = "id of feedback") Long id) {
        return feedbackService.resolveFeedback(id);
    }

    @Operation(
            summary = "Delete feedback by id",
            security = @SecurityRequirement(name = "bearerAuth")

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Delete feedback successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Delete feedback successfully!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Delete the feedback failed!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Delete the feedback failed!",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found the feedback!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Not found the feedback!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @DeleteMapping("/feedbacks/{id}")
    public ResponseEntity<Response> deleteFeedback(@PathVariable @Schema(description = "id of feedback") Long id) {
        return feedbackService.deleteById(id);
    }

    @GetMapping("/voucher-sets")
    public ResponseEntity<Response> getAllVoucherSets(@RequestParam(defaultValue = "0", required = false) Integer page,
                                                      @RequestParam(defaultValue = "0", required = false) Integer elementsPerPage,
                                                      @RequestParam(defaultValue = "createdAt", required = false) String filter,
                                                      @RequestParam(defaultValue = "desc", required = false) String sortBy) {
        if (elementsPerPage == 0) {
            elementsPerPage = Integer.parseInt(defaultElementPerPage);
        }
        return promotionService.getAllVoucherSets(page, elementsPerPage, filter, sortBy);
    }

    @Operation(
            summary = "Create promotion",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreatePromotionRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "code": "PROMO1",
                                        "description": "Promotion 1",
                                        "percent": 12,
                                        "storeId": 35
                                    }
                                    """)
                    )
            )

    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Create promotion successfully!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Create promotion successfully",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Create promotion failed!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Create promotion failed!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @PostMapping("/create-voucher-set")
    public ResponseEntity<Response> createVoucherSet(@RequestBody CreatePromotionRequest promotionRequest) {
        return promotionService.createVoucherSet(promotionRequest);
    }

    @DeleteMapping("/voucher-sets/{id}")
    public ResponseEntity<Response> deleteVoucherSet(@PathVariable Long id) {
        return promotionService.deleteVoucherSet(id);
    }

    @GetMapping("/voucher-sets/{id}")
    public ResponseEntity<Response> getVoucherSetById(@PathVariable Long id) {
        return promotionService.getVoucherSetById(id);
    }

    @DeleteMapping("/vouchers/{id}")
    public ResponseEntity<Response> deleteVoucher(@PathVariable Long id) {
        return promotionService.deleteVoucherById(id);
    }

    @PutMapping("/voucher-sets/{id}")
    public ResponseEntity<Response> updateVoucherSet(@PathVariable Long id, @RequestBody UpdatePromotionRequest promotionRequest) {
        return promotionService.updateVoucherSet(id, promotionRequest);
    }

    @PutMapping("/voucher-sets/{id}/add")
    public ResponseEntity<Response> addVoucherToSet(@PathVariable Long id, @RequestParam int quantity) {
        return promotionService.addVoucherToSet(id, quantity);
    }

    @PutMapping("/voucher-sets/{id}/subtract")
    public ResponseEntity<Response> subtractVoucherToSet(@PathVariable Long id, @RequestParam int quantity) {
        return promotionService.subtractVoucherToSet(id, quantity);
    }

    @GetMapping("/voucher-sets/{id}/all")
    public ResponseEntity<Response> getAllVouchersInSet(@PathVariable Long id,
                                                        @RequestParam(defaultValue = "0", required = false) Integer page,
                                                        @RequestParam(defaultValue = "0", required = false) Integer elementsPerPage,
                                                        @RequestParam(defaultValue = "all", required = false) String status,
                                                        @RequestParam(defaultValue = "createdAt", required = false) String filter,
                                                        @RequestParam(defaultValue = "desc", required = false) String sortBy) {
        if (elementsPerPage == 0) {
            elementsPerPage = Integer.parseInt(defaultElementPerPage);
        }
        return promotionService.getAllVouchersInSet(id, page, elementsPerPage, status, filter, sortBy);
    }



    @Operation(
            summary = "Create payment option",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateDeliveryPartnerRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "name": "Quick and easy delivery Inc."
                                    }
                                    """)
                    )
            )

    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Create delivery partner successfully!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 200,
                                                "message": "Create delivery partner successfully",
                                                "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Create delivery partner failed!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Wrong information of delivery partner!",
                                                "data": null
                                            }
                                            """)
                            )
                    )
            }
    )
    @PostMapping("/app-setting/delivery-partners")
    public ResponseEntity<Response> createDeliveryPartner(@RequestBody CreateDeliveryPartnerRequest deliveryPartnerRequest) {
        return deliveryPartnerService.createDeliveryPartner(deliveryPartnerRequest);
    }

    @GetMapping("/account")
    public ResponseEntity<Response> getAccountInfo() {
        User currentAdmin = getCurrentAdmin();
        return userService.getUserInformationById(currentAdmin.getId());
    }


    @PutMapping("/account")
    public ResponseEntity<Response> updateAccountInfo(@RequestBody UpdateAdminRequest updateAccountRequest) {
        User currentAdmin = getCurrentAdmin();
        return userService.updateAccountInfoById(currentAdmin.getId(), updateAccountRequest);
    }
    @GetMapping("/search-user-by-name")
    public ResponseEntity<Response> searchUserByName(@RequestParam String name,
                                                     @RequestParam(defaultValue = "0", required = false) Integer page,
                                                     @RequestParam(defaultValue = "0", required = false) Integer elementsPerPage,
                                                     @RequestParam(defaultValue = "all", required = false) String status,
                                                     @RequestParam(defaultValue = "createdAt", required = false) String filter,
                                                     @RequestParam(defaultValue = "desc", required = false) String sortBy,
                                                     @RequestParam(defaultValue = "all", required = false) String role) {
        if (elementsPerPage == 0) {
            elementsPerPage = Integer.parseInt(defaultElementPerPage);
        }
        return userService.searchUserByName(name, page, elementsPerPage,status, filter, sortBy, role);
    }
    @GetMapping("/search-user-by-email")

    public ResponseEntity<Response> searchUserByEmail(@RequestParam String email) {
        return userService.searchUserByEmail(email);
    }
    public User getCurrentAdmin() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
