using API.Foodie.Interfaces;
using API.Foodie.Interfaces.Data;

namespace API.Foodie.Controllers;

public class OrderController : BaseApiController
{
    private readonly IUnitOfWork _unitOfWork;
    private readonly IMapper _mapper;
    private readonly IMailerService _mailer;

    public OrderController(IUnitOfWork unitOfWork, IMapper mapper, IMailerService mailer)
    {
        _unitOfWork = unitOfWork;
        _mapper = mapper;
        _mailer = mailer;
    }
}
